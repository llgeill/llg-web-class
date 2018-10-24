package com.eill.llg;

import com.eill.llg.pojo.Operation;
import com.eill.llg.service.FourArithmeticOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    private FourArithmeticOperation fourArithmeticOperation;

    @RequestMapping("hello")
    public String hello(){
        return "hellosss";
    }

    @RequestMapping("getOperation")
    public List<Operation> getOperation(int count){
        List<String> resultList=new ArrayList<>();
        List<String> problemList=new ArrayList<>();
        List<Integer> signNumberList=new ArrayList<>();
        fourArithmeticOperation.outPutData(count,resultList,problemList,signNumberList);
        List<Operation> list=new ArrayList<>();
        for(int i=0;i<resultList.size();i++){
            Operation operation=new Operation();
            operation.setAnswer(resultList.get(i));
            operation.setProblem(problemList.get(i));
            operation.setSignConunt(signNumberList.get(i));
            list.add(operation);
        }
        return list;
    }
}
