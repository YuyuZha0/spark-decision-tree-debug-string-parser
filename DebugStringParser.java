package com.ifeng.dmp.ctrp.ml;

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

    public static void main(String[] args) {
        String s = "If (feature 9 <= 0.0125)\n" +
                "     If (feature 10 <= 0.0114)\n" +
                "      If (feature 12 <= 0.0075)\n" +
                "       If (feature 0 <= 0.0065)\n" +
                "        If (feature 1 <= 0.0058)\n" +
                "         Predict: 0.047923389851888445\n" +
                "        Else (feature 1 > 0.0058)\n" +
                "         Predict: 0.07137635126022983\n" +
                "       Else (feature 0 > 0.0065)\n" +
                "        If (feature 12 <= 0.0055)\n" +
                "         Predict: 0.08800853325349002\n" +
                "        Else (feature 12 > 0.0055)\n" +
                "         Predict: 0.11735270545200469\n" +
                "      Else (feature 12 > 0.0075)\n" +
                "       If (feature 0 <= 0.0093)\n" +
                "        If (feature 7 <= 0.0101)\n" +
                "         Predict: 0.10974269542143679\n" +
                "        Else (feature 7 > 0.0101)\n" +
                "         Predict: 0.14264542094310068\n" +
                "       Else (feature 0 > 0.0093)\n" +
                "        If (feature 6 <= 0.0132)\n" +
                "         Predict: 0.15816845060656223\n" +
                "        Else (feature 6 > 0.0132)\n" +
                "         Predict: 0.22484364604125084\n" +
                "     Else (feature 10 > 0.0114)\n" +
                "      If (feature 0 <= 0.0149)\n" +
                "       If (feature 7 <= 0.011)\n" +
                "        If (feature 2 <= 0.0199)\n" +
                "         Predict: 0.17659115093907074\n" +
                "        Else (feature 2 > 0.0199)\n" +
                "         Predict: 0.11897248764689246\n" +
                "       Else (feature 7 > 0.011)\n" +
                "        If (feature 10 <= 0.0137)\n" +
                "         Predict: 0.19971164036377678\n" +
                "        Else (feature 10 > 0.0137)\n" +
                "         Predict: 0.23499119198104446\n" +
                "      Else (feature 0 > 0.0149)\n" +
                "       If (feature 2 <= 0.0355)\n" +
                "        If (feature 10 <= 0.0169)\n" +
                "         Predict: 0.19316578816705413\n" +
                "        Else (feature 10 > 0.0169)\n" +
                "         Predict: 0.27050388273012166\n" +
                "       Else (feature 2 > 0.0355)\n" +
                "        If (feature 1 <= 0.0164)\n" +
                "         Predict: 0.10299145299145299\n" +
                "        Else (feature 1 > 0.0164)\n" +
                "         Predict: 0.14485303437882907\n" +
                "    Else (feature 9 > 0.0125)\n" +
                "     If (feature 12 <= 0.0222)\n" +
                "      If (feature 12 <= 0.0025)\n" +
                "       If (feature 3 <= 0.0136)\n" +
                "        If (feature 4 <= 0.0163)\n" +
                "         Predict: 0.16205533596837945\n" +
                "        Else (feature 4 > 0.0163)\n" +
                "         Predict: 0.07920792079207921\n" +
                "       Else (feature 3 > 0.0136)\n" +
                "        If (feature 9 <= 0.2019)\n" +
                "         Predict: 0.9225040850767459\n" +
                "        Else (feature 9 > 0.2019)\n" +
                "         Predict: 0.5019334880123744\n" +
                "      Else (feature 12 > 0.0025)\n" +
                "       If (feature 3 <= 0.0759)\n" +
                "        If (feature 7 <= 0.0217)\n" +
                "         Predict: 0.20286529220528218\n" +
                "        Else (feature 7 > 0.0217)\n" +
                "         Predict: 0.7116316639741519\n" +
                "       Else (feature 3 > 0.0759)\n" +
                "        If (feature 12 <= 0.0082)\n" +
                "         Predict: 0.1456244234832029\n" +
                "        Else (feature 12 > 0.0082)\n" +
                "         Predict: 0.6139024177696873\n" +
                "     Else (feature 12 > 0.0222)\n" +
                "      If (feature 3 <= 0.0136)\n" +
                "       If (feature 0 <= 0.0149)\n" +
                "        If (feature 14 <= 0.0089)\n" +
                "         Predict: 0.11330472103004292\n" +
                "        Else (feature 14 > 0.0089)\n" +
                "         Predict: 0.16452830188679246\n" +
                "       Else (feature 0 > 0.0149)\n" +
                "        If (feature 11 <= 0.0167)\n" +
                "         Predict: 0.17938517179023508\n" +
                "        Else (feature 11 > 0.0167)\n" +
                "         Predict: 0.27445605619325\n" +
                "      Else (feature 3 > 0.0136)\n" +
                "       If (feature 2 <= 0.0355)\n" +
                "        If (feature 4 <= 0.0186)\n" +
                "         Predict: 0.7787088347055098\n" +
                "        Else (feature 4 > 0.0186)\n" +
                "         Predict: 0.9376800209478922\n" +
                "       Else (feature 2 > 0.0355)\n" +
                "        If (feature 3 <= 0.0759)\n" +
                "         Predict: 0.9172398148052672\n" +
                "        Else (feature 3 > 0.0759)\n" +
                "         Predict: 0.985060246603449";
        DebugStringParser parser = new DebugStringParser(s);
        System.out.println(parser.parseAndGetRootNode());
    }

}
