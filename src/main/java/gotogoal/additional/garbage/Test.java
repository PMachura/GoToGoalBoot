///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package gotogoal.additional.garbage;
//
//import org.springframework.data.domain.Sort;
//import org.springframework.data.domain.Sort.Order;
//
///**
// *
// * @author Przemek
// */
//public class Test {
//
//    public static void main(String[] args) {
//        for (Sort.Direction c : Sort.Direction.values()) {
//      //      System.out.println(c);
//        }
//        //System.out.println(Sort.Direction.valueOf("DESC"));
//       // System.out.println(Sort.Direction.fromStringOrNull("DESC"));
//        
//        Sort sort = new Sort(Sort.Direction.fromStringOrNull("desc"), "date");
//        Order order = sort.getOrderFor("date");
//        System.out.println(order.getDirection());
//    }
//}
