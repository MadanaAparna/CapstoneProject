package com.capstone.utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {
   private static ExtentReports extent;
   static String projectpath = System.getProperty("user.dir");

   public static ExtentReports getinstance() {
       if (extent == null) {
           String reportpath = projectpath + "\\src\\test\\resources\\Reports\\projectreport.html";
           ExtentSparkReporter spark = new ExtentSparkReporter(reportpath);
           extent = new ExtentReports();  // <-- Initialize extent here!
           extent.attachReporter(spark);
       }
       return extent;
   }
}