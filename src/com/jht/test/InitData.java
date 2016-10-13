package com.jht.test;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

public class InitData extends AbstractJavaSamplerClient{

	private Connection conn;
	private static PreparedStatement pst = null;
	
	
	/**
	 * 插入相关数据
	 */
	@Override
	public SampleResult runTest(JavaSamplerContext context) {
		// TODO Auto-generated method stub
		SampleResult sr = new SampleResult();
		sr.setSampleLabel("InitData");
		String prefix=context.getParameter("prefix");
		String password=context.getParameter("password");
		String times=context.getParameter("times");
		try {
			if(conn!=null && !conn.isClosed()){
				sr.sampleStart();
				initSubSystem(prefix,password,Integer.parseInt(times));
				
				sr.setSuccessful(true);
				sr.sampleEnd();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sr;
	}

	/**
	 * 实现数据连接的获取
	 */
	@Override
	public void setupTest(JavaSamplerContext context) {
		// TODO Auto-generated method stub
		super.setupTest(context);
		conn=DBHelper.getConnection();
	}

	private void initSubSystem(String prefix,String password,int times) throws SQLException{
		String sql="INSERT INTO `NP_CF_SUBSYSTEM` (`ID`, `CODE`, `NAME`, `STATUS`, `DEFAULT_PASS`) VALUES(?,?,?,?,?)";
		String id=null,code=null,name=null,status="NORMAL",default_pass=MD5Util.string2MD5(password);
		
		for(int i=0;i<times;i++){
			id=UUID.randomUUID().toString().replace("-", "");
			code=prefix+i;
			name=prefix+i;
			pst = conn.prepareStatement(sql);
			pst.setString(1, id);
			pst.setString(2, code);
			pst.setString(3, name);
			pst.setString(4, status);
			pst.setString(5, default_pass);
			pst.executeUpdate();
		}
		
	}

	@Override
	public void teardownTest(JavaSamplerContext context) {
		// TODO Auto-generated method stub
		super.teardownTest(context);
		//关闭数据
		DBHelper.closeConnection(conn);
	}

	@Override
	public Arguments getDefaultParameters() {
		// TODO Auto-generated method stub
		
		Arguments  arg=new Arguments();
		arg.addArgument("prefix", "请输入子系统编号前缀");
		arg.addArgument("password","请输入子系统openfire密码");
		arg.addArgument("times", "请输入需要初始化的记录数量");
		return arg;
	}
	

}
