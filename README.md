# spark-decision-tree-debug-string-parser
a parser of spark decision tree on random forest or gradient boosted trees

#### example:
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
