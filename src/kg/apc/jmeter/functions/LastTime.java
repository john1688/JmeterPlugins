package kg.apc.jmeter.functions;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
//import org.apache.jmeter.threads.JMeterVariables;

public class LastTime extends AbstractFunction {
    
    //冗余2分钟
    private long times = 1000 * 60 * 2;

    @SuppressWarnings("unchecked")
    private static final List<String> desc = new LinkedList();
    private static final String       KEY  = "__LastTime";
    private Object[]                  values;

    public synchronized String execute(SampleResult paramSampleResult, Sampler paramSampler)
                                                                                            throws InvalidVariableException {
//        JMeterVariables localJMeterVariables = getVariables();
        String clientKey = ((CompoundVariable) this.values[0]).execute();

        String text = (System.currentTimeMillis()-times)+"";
        String result;
        try {
            result = AESUtil.encodeText(clientKey, text);
        } catch (Exception e) {
            e.printStackTrace();
            return "error:"+e.getMessage();
        }

//        if ((localJMeterVariables != null) && (this.values.length > 1)) {
//            String str3 = ((CompoundVariable) this.values[1]).execute().trim();
//            localJMeterVariables.put(str3, result);
//        }

        return result;
    }

    public synchronized void setParameters(Collection<CompoundVariable> paramCollection)
                                                                                        throws InvalidVariableException {
        checkMinParameterCount(paramCollection, 1);
        this.values = paramCollection.toArray();
    }

    public String getReferenceKey() {
        return KEY;
    }

    public List<String> getArgumentDesc() {
        return desc;
    }

    static {
        desc.add("生成SSO时间戳");
        desc.add("请输入clientKey");
    }
    
    public static void main(String args[]) throws Exception{
        
      if( args==null || args.length!=3 ){
          System.out.println("please example: LastTime enc [clientKey] [text] \r\n LastTime dec [clientKey] [text]");
          return;
      }
      
      System.out.println("args: "+ args[0]+" "+args[1]+" "+args[2]);
      
      if( "enc".equals(args[0]) ){
          String text = AESUtil.encodeText(args[1], args[2]);
          System.out.println("encrypt:"+text);
      }else if( "dec".equals(args[0]) ){
          String text = AESUtil.decodeText(args[1], args[2]);
          System.out.println("decrypt:"+text);
      }else{
          System.out.println("please example: LastTime enc 123 \r\n LastTime dec 456");
      }
      
      
  }
}
