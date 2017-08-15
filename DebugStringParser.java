
import javax.annotation.concurrent.NotThreadSafe;

/**
 * Created by zhaoyy on 2017/8/14.
 */
@NotThreadSafe
public final class DebugStringParser {

    private int lookahead = 0;
    private final int len;
    private final String source;

    public DebugStringParser(String s) {
        if(s == null || s.isEmpty())
            throw new IllegalArgumentException("empty string!");
        this.source = s;
        this.len = s.length();
    }

    /**
     * Grammar:
     <p>S -> ' '*</p>
     <p>IF -> If (feature INT <= DOUBLE)</p>
     <p>ELSE -> Else (feature INT > DOUBLE)</p>
     <p>PREDICT -> Predict: DOUBLE</p>
     <p>INT -> [+-]([1-9][0-9]+|0)</p>
     <p>DOUBLE -> INT(\.[0-9]+)?([eE]INT)?</p>

     <p>TREE -> IF\nTREE\nELSE\nTREE | PREDICT </p>
     * @return
     */
    public Node parseAndGetRootNode(){
        lookahead = 0;
        return mathTree();
    }

    private Node mathTree(){
        matchSpaces();
        if(lookahead <len && source.charAt(lookahead) == 'P'){
            double value =  matchPredict();
            return new Node(null,null,true,-1,value);
        }
        ConditionEntry c1 = matchCondition(true);
        matchLineBreaker();
        matchSpaces();
        Node left = mathTree();
        matchLineBreaker();
        matchSpaces();
        ConditionEntry c2 = matchCondition(false);
        if(c1.index != c2.index || c1.value != c2.value)
            throw new IllegalArgumentException("condition not match!");
        matchLineBreaker();
        Node right = mathTree();
        return new Node(left,right,false,c1.index,c1.value);
    }

    private void matchSpaces(){
        while(lookahead < len && source.charAt(lookahead) == ' ')
            lookahead ++;
    }

    private void matchLineBreaker(){
        if(lookahead >= len || source.charAt(lookahead++) != '\n')
            throw new IllegalArgumentException("line breaker is required.");
    }

    private ConditionEntry matchCondition(boolean isIfBranch){
        if(isIfBranch)
            matchString("If (feature ");
        else
            matchString("Else (feature ");
        int mark = lookahead;
        matchInt();
        int index = Integer.parseInt(source.substring(mark,lookahead));
        if(isIfBranch)
            matchString(" <= ");
        else
            matchString(" > ");
        mark = lookahead;
        matchDouble();
        double value = Double.parseDouble(source.substring(mark,lookahead));
        if(lookahead >= len || source.charAt(lookahead++)!=')')
            throw new IllegalArgumentException("')' is required.");
        return new ConditionEntry(index,value);
    }

    private static final class ConditionEntry{
        final int index;
        final double value;

        ConditionEntry(int index, double value) {
            this.index = index;
            this.value = value;
        }
    }

    private double matchPredict(){
        matchString("Predict: ");
        int mark = lookahead;
        matchDouble();
        return Double.parseDouble(source.substring(mark,lookahead));
    }

    private void matchInt(){
        char c;
        if(lookahead < len && ((c=source.charAt(lookahead)) == '+' || c == '-'))
            lookahead ++;
        if(lookahead <len && source.charAt(lookahead) == '0'){
            lookahead ++;
            return;
        }
        if(lookahead >= len || ((c=source.charAt(lookahead ++)) < '1') || c > '9')
            throw new IllegalArgumentException("[1-9] is expected.");
        while(lookahead < len && (c=source.charAt(lookahead)) >= '0' && c <='9')
            lookahead++;
    }

    private void matchDouble(){
        char c;
        matchInt();
        if(lookahead < len && source.charAt(lookahead) == '.'){
            lookahead ++;
            while(lookahead < len && (c=source.charAt(lookahead)) >= '0' && c <='9')
                lookahead++;
        }
        if(lookahead <len && ((c = source.charAt(lookahead)) == 'e' || c == 'E')){
            lookahead++;
            matchInt();
        }
    }

    private void matchString(String s){
        for(int i=0,j=s.length();i<j;i++){
            if(lookahead >= len || s.charAt(i) != source.charAt(lookahead++))
                throw new IllegalArgumentException("expect '" + s + "' at " + (lookahead-i));
        }
    }

}
