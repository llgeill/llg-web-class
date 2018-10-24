package com.eill.llg.service;

import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * 任何编程语言都可以，命令行程序接受一个数字输入，然后输出相应数目的四则运算题目和答案。
 * 例如输入数字是 30，那就输出 30 道题目和答案。 运算式子必须至少有两个运算符，运算数字
 * 是在 100 之内的正整数，答案不能是负数。 如：
 * 23 - 3 * 4 = 11
 */
@Service
public class FourArithmeticOperation {
    private static Logger logger=Logger.getLogger("FourArithmeticOperation");
    private static int count;


    /**
     * 判断数据为整数并且大于0，否则递归调用直到输入正确整数
     * @return 输入数目
     */
    public void readValue(){
        try {
            System.out.print("请输入你需要的算式个数：");
            Scanner scanner=new Scanner(System.in);
            count=scanner.nextInt();
            if(count<=0)throw new Exception();
        }catch (Exception e){
            logger.log(Level.WARNING,"数据输入错误，请输入整数并且大于0");
            readValue();
        }
    }

    /**
     * 输入答案，以逗号分隔开来
     * @return
     */
    public String[] inputResult(){
        System.out.println("请按照顺序输入您的答案，并且以','分隔开来（分数需要化为最简）：");
        Scanner scanner=new Scanner(System.in);
        String result=scanner.next();
        String[] strings=result.split(",");
        return strings;
    }

    /**
     * 自动得分
     * @param strings
     * @param resultList
     */
    public void checkTest(String[] strings,List<String> resultList){
        int count=0;
        for(int i=0;i<strings.length;i++){
            if(strings[i].equals(resultList.get(i))) count++;
        }
        System.out.print("答案：");
        for(String result:resultList){
            System.out.print(result+" , ");
        }
        System.out.println("");
        System.out.println("您的最终得分是："+computeScope(count,resultList.size()));
    }


    /**
     * 输出规定的算式表达式和算式结果
     *
     * @param count
     */
    public void outPutData(int count,List<String> resultList,List<String> problemList,List<Integer> signNumberList){
        String sign[]={"+","-","÷","×"};
        String finallyResult="";
        int signNumber=-1;
        for(int i=0;i<count;i++){
            List<String> signList=new ArrayList();
            List<String> arithmeticValue=new ArrayList<>();
            //随机生成数量不等的运算符
            signNumber=new Random().nextInt(2)+2;

            for(int j=0;j<signNumber;j++){
                int signIndex=new Random().nextInt(4);
                signList.add(sign[signIndex]);
            }
            //随机生成一个分数并且得出分数位置
            int fraction=new Random().nextInt(signList.size());
            //随机生成比运算符还多一位的数值
            for(int k=0;k<=signList.size();k++){
                //
                if(k==fraction){
                    int denominator=new Random().nextInt(28)+2;
                    int molecule=new Random().nextInt(denominator)+1;
                    arithmeticValue.add("#"+molecule+"/"+denominator);
                }else {
                    int value=new Random().nextInt(29)+1;
                    arithmeticValue.add(Integer.toString(value));
                }

            }
            //拼接运算表达式
            StringBuilder stringBuilder=new StringBuilder();
            for(int n=0;n<arithmeticValue.size();n++){
                if(arithmeticValue.get(n).startsWith("#")){
                    stringBuilder.append(arithmeticValue.get(n).substring(1));
                }else {
                    stringBuilder.append(arithmeticValue.get(n));
                }
                if(n==arithmeticValue.size()-1)break;
                stringBuilder.append(signList.get(n));
            }
            finallyResult=computeResult(signList,arithmeticValue);
            if(finallyResult.startsWith("#")){
                finallyResult=finallyResult.substring(1);
            }
            if(finallyResult.startsWith("-")){
                i--;
                continue;
            }else{
                //将答案添加到数组上
                resultList.add(fraction(finallyResult));
                problemList.add(stringBuilder.toString());
                signNumberList.add(signNumber);
                System.out.println(stringBuilder.toString() + "  :  "+fraction(finallyResult));
            }




        }
    }

    /**
     * 通过栈运算得出算数表达式结果
     * @param signList 符号列表
     * @param arithmeticValue 整数列表
     */
    public String computeResult(List<String> signList,List<String> arithmeticValue){
        Map<String,Integer> map=new HashMap<>();
        map.put("+",1);
        map.put("-",1);
        map.put("×",2);
        map.put("÷",2);
        Stack <String> numberValue=new Stack<>();
        Stack <String> signValue=new Stack<>();
        for(int i=0;i<signList.size();i++){
            if(i==0) {
                numberValue.push(arithmeticValue.get(i));
                signValue.push(signList.get(i));
            }else{
                numberValue.push(arithmeticValue.get(i));
                //当前符号等级
                int thisLevel=map.get(signList.get(i));
                //栈顶层符号等级
                int thatLevel=map.get(signValue.peek());
                if(thisLevel>thatLevel){
                    signValue.push(signList.get(i));
                }else{
                    //出栈顺序与运算顺序相反,除法需要分清楚
                    String oneStr=numberValue.pop();
                    String twoStr=numberValue.pop();
                    String resultStr=computeOptions(twoStr,oneStr,signValue.pop());
                    numberValue.push(resultStr);
                    signValue.push(signList.get(i));
                }
            }

        }
        numberValue.push(arithmeticValue.get(signList.size()));
        while (!signValue.isEmpty()){
            String oneStr=numberValue.pop();
            String twoStr=numberValue.pop();
            String resultStr=computeOptions(twoStr,oneStr,signValue.pop());
            numberValue.push(resultStr);
        }
        return numberValue.pop();
    }

    /**
     *
     * 由于需要考虑分数的情况，所以直接将所有整数化成分数的方式直接运算
     *
     * @param one 运算数值
     * @param two 运算数值
     * @param options 运算符号
     * @return  运算结果
     */
    public String computeOptions(String one,String two, String options){
        String resultStr="";
        switch (options){
            case "+":
                if(one.startsWith("#")){
                    //获取第一个分数的分子和分母
                    String oneMolecule=one.substring(1,one.indexOf("/"));
                    String oneDenominator=one.substring(one.indexOf("/")+1);
                    if(two.startsWith("#")){
                        //获取第二个分数的分子和分母
                        String twoMolecule=two.substring(1,two.indexOf("/"));
                        String twoDenominator=two.substring(two.indexOf("/")+1);
                        //计算两个数的和，通过分母相乘分子相加的方式
                        int totalMolecule=Integer.parseInt(oneMolecule)*Integer.parseInt(twoDenominator)+
                                Integer.parseInt(twoMolecule)*Integer.parseInt(oneDenominator);
                        int totalDenominator=Integer.parseInt(oneDenominator)*Integer.parseInt(twoDenominator);
                        resultStr="#"+totalMolecule+"/"+totalDenominator;
                    }else{
                        int totalMolecule=Integer.parseInt(oneMolecule)+Integer.parseInt(oneDenominator)*Integer.parseInt(two);
                        resultStr="#"+String.valueOf(totalMolecule)+"/"+oneDenominator;
                    }
                }else if(!one.startsWith("#")&&two.startsWith("#")){
                    String twoMolecule=two.substring(1,two.indexOf("/"));
                    String twoDenominator=two.substring(two.indexOf("/")+1);
                    int totalMolecule=Integer.parseInt(twoMolecule)+Integer.parseInt(twoDenominator)*Integer.parseInt(one);
                    resultStr="#"+String.valueOf(totalMolecule)+"/"+twoDenominator;
                }else {
                    int resultAdd = Integer.parseInt(one) + Integer.parseInt(two);
                    resultStr = String.valueOf(resultAdd);
                }
                break;
            case "-":
                if(one.startsWith("#")){
                    String oneMolecule=one.substring(1,one.indexOf("/"));
                    String oneDenominator=one.substring(one.indexOf("/")+1);
                    if(two.startsWith("#")){
                        String twoMolecule=two.substring(1,two.indexOf("/"));
                        String twoDenominator=two.substring(two.indexOf("/")+1);
                        int totalMolecule=Integer.parseInt(oneMolecule)*Integer.parseInt(twoDenominator)-
                                Integer.parseInt(twoMolecule)*Integer.parseInt(oneDenominator);
                        int totalDenominator=Integer.parseInt(oneDenominator)*Integer.parseInt(twoDenominator);
                        resultStr="#"+totalMolecule+"/"+totalDenominator;
                    }else{
                        int totalMolecule=Integer.parseInt(oneMolecule)-Integer.parseInt(oneDenominator)*Integer.parseInt(two);
                        resultStr="#"+String.valueOf(totalMolecule)+"/"+oneDenominator;
                    }
                }else if(!one.startsWith("#")&&two.startsWith("#")){
                    String twoMolecule=two.substring(1,two.indexOf("/"));
                    String twoDenominator=two.substring(two.indexOf("/")+1);
                    int totalMolecule=Integer.parseInt(twoDenominator)*Integer.parseInt(one)-Integer.parseInt(twoMolecule);
                    resultStr="#"+String.valueOf(totalMolecule)+"/"+twoDenominator;
                }else {
                    int resultSubs = Integer.parseInt(one) - Integer.parseInt(two);
                    resultStr = String.valueOf(resultSubs);
                }
                break;
            case "×":
                if(one.startsWith("#")){
                    String oneMolecule=one.substring(1,one.indexOf("/"));
                    String oneDenominator=one.substring(one.indexOf("/")+1);
                    if(two.startsWith("#")){
                        String twoMolecule=two.substring(1,two.indexOf("/"));
                        String twoDenominator=two.substring(two.indexOf("/")+1);
                        int totalMolecule=Integer.parseInt(oneMolecule) *Integer.parseInt(twoMolecule);
                        int totalDenominator=Integer.parseInt(oneDenominator)*Integer.parseInt(twoDenominator);
                        resultStr="#"+totalMolecule+"/"+totalDenominator;
                    }else{
                        int totalMolecule=Integer.parseInt(oneMolecule)*Integer.parseInt(two);
                        resultStr="#"+String.valueOf(totalMolecule)+"/"+oneDenominator;
                    }
                }else if(!one.startsWith("#")&&two.startsWith("#")){
                    String twoMolecule=two.substring(1,two.indexOf("/"));
                    String twoDenominator=two.substring(two.indexOf("/")+1);
                    int totalMolecule=Integer.parseInt(twoMolecule)*Integer.parseInt(one);
                    resultStr="#"+String.valueOf(totalMolecule)+"/"+twoDenominator;
                }else {
                    int resultSubs = Integer.parseInt(one) * Integer.parseInt(two);
                    resultStr = String.valueOf(resultSubs);
                }
                break;
            case "÷":
                if(one.startsWith("#")){
                    String oneMolecule=one.substring(1,one.indexOf("/"));
                    String oneDenominator=one.substring(one.indexOf("/")+1);
                    if(two.startsWith("#")){
                        String twoMolecule=two.substring(1,two.indexOf("/"));
                        String twoDenominator=two.substring(two.indexOf("/")+1);
                        int totalMolecule=Integer.parseInt(oneMolecule) *Integer.parseInt(twoDenominator);
                        int totalDenominator=Integer.parseInt(oneDenominator)*Integer.parseInt(twoMolecule);
                        resultStr="#"+totalMolecule+"/"+totalDenominator;
                    }else{
                        int totalMolecule=Integer.parseInt(oneMolecule);
                        int totalDenominator=Integer.parseInt(oneDenominator)*Integer.parseInt(two);
                        resultStr="#"+String.valueOf(totalMolecule)+"/"+totalDenominator;
                    }
                }else if(!one.startsWith("#")&&two.startsWith("#")){
                    String twoMolecule=two.substring(1,two.indexOf("/"));
                    String twoDenominator=two.substring(two.indexOf("/")+1);
                    int totalMolecule=Integer.parseInt(twoDenominator)*Integer.parseInt(one);
                    int totalDenominator=Integer.parseInt(twoMolecule);
                    resultStr="#"+String.valueOf(totalMolecule)+"/"+totalDenominator;
                }else {
                    resultStr="#"+one+"/"+two;
                }
                break;

        }
        return resultStr;
    }

    /**
     * 获取分数的最大公因子
     * @param finallyResult 未通分的分数
     * @return
     */
    public String fraction(String finallyResult){
        String fr="";
        String oneMolecule=finallyResult.substring(0,finallyResult.indexOf("/"));
        String oneDenominator=finallyResult.substring(finallyResult.indexOf("/")+1);
        int smaller = Integer.parseInt(oneMolecule) > Integer.parseInt(oneDenominator) ? Integer.parseInt(oneDenominator) : Integer.parseInt(oneMolecule);
        int commonFactor=1;
        for (int i = 1; i <= smaller; i++) {
            if ( Integer.parseInt(oneMolecule) % i == 0 && Integer.parseInt(oneDenominator) % i == 0) {
                commonFactor = i;
            }
        }
        if(Integer.parseInt(oneDenominator)/commonFactor==1){
            return Integer.parseInt(oneMolecule)/commonFactor+"";
        }else {
            return Integer.parseInt(oneMolecule) / commonFactor + "/" + Integer.parseInt(oneDenominator) / commonFactor;
        }
    }


    /**
     * 计算百分比的分数
     * @param num1
     * @param num2
     * @return
     */
    public String computeScope(int num1,int num2){
        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(0);
        String result = numberFormat.format((float) num1 / (float) num2 * 100);
        return result;
    }
}
