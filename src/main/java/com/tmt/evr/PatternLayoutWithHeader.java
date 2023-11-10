package com.tmt.evr;
import org.apache.log4j.PatternLayout;

// Custom Layout
public class  PatternLayoutWithHeader extends PatternLayout {
//   private String header = "date | event | evrEvent | evrCategory | evrCause | evrReason | RequestIP (wrapper) | request Method | ApiName | Txn Id | Req Node (SRM) | operationType | request Txn Id | Http Response Code | status Code | error Code | description | Elapsed Time for request";

   private String header = null;
   @Override
      public String getHeader()
      {
         return header+"\n";
      }

      public void setHeader(String header)
      {
         this.header = header;
      }
      
      
}
