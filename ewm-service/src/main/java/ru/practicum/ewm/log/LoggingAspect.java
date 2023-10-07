package ru.practicum.ewm.log;

import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Around("@within(ru.practicum.ewm.log.Logged)")
    public Object logMethodCallAndReturnedValue(ProceedingJoinPoint jp) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) jp.getSignature();
        Parameter[] parameters = methodSignature.getMethod().getParameters();
        Object[] args = jp.getArgs();
        List<String> stringArgs = Arrays.stream(args)
                .map(arg -> {
                    if (arg == null) {
                        return "null";
                    } else {
                        return arg.toString();
                    }
                })
                .collect(Collectors.toList());
        StringBuilder paramNamesAndValues = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            paramNamesAndValues.append(parameters[i].getName()).append(" = ").append(stringArgs.get(i)).append(", ");
        }
        String methodName = methodSignature.getName();
        log.info("Вызов метода: " + methodName + " с параметрами: " + paramNamesAndValues);
        Object returnedValue = jp.proceed();
        log.info("Вернули: " + returnedValue.toString());
        return returnedValue;
    }
}