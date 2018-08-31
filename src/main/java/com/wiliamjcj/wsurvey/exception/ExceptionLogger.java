package com.wiliamjcj.wsurvey.exception;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wiliamjcj.wsurvey.utils.Jsonable;

@Component
public class ExceptionLogger implements Jsonable {

	@JsonIgnore
	private transient final Logger log = LoggerFactory.getLogger(this.getClass());

	@JsonIgnore
	@Value("${logging.printstatcktrace}")
	private String printStackTrace;
	
	@JsonIgnore
	@Value("${logging.printfullstatcktrace}")
	private String printFullStackTrace;
	
	@JsonIgnore
	@Value("${application.base.package}")
	private String packageName;  
	

	private List<StackTraceElement> stackTrace;
	private String message;
	private String ExceptionType;

	public void error(Exception e) {
		try {
			List<StackTraceElement> stackTraceAsList = Arrays.asList(e.getStackTrace());
			
			if(Boolean.parseBoolean(printFullStackTrace)) {
				e.printStackTrace();
			}else if (Boolean.parseBoolean(printStackTrace)) {
				e.setStackTrace(stackTraceAsList.stream()
						.filter(st -> st.getClassName().contains(packageName))
						.toArray(StackTraceElement[]::new)
				);
				e.printStackTrace();
			}

			ExceptionType = e.getClass().getName();
			message = e.getMessage();
			stackTrace = stackTraceAsList.stream().filter(st -> st.getClassName().contains(packageName))
					.map(el -> new StackTraceElement(el.getClassName(), el.getMethodName(), null, el.getLineNumber()))
					.collect(Collectors.toList());

			log.error(this.toPrettyJson());
		} catch (Exception e2) {
			e2.printStackTrace();
			log.error(e.getMessage());
		}
	}

	public List<StackTraceElement> getStackTrace() {
		return stackTrace;
	}

	public void setStackTrace(List<StackTraceElement> stackTrace) {
		this.stackTrace = stackTrace;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getExceptionType() {
		return ExceptionType;
	}

	public void setExceptionType(String exceptionType) {
		ExceptionType = exceptionType;
	}
	
}
