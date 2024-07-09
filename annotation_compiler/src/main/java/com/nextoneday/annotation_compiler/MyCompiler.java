package com.nextoneday.annotation_compiler;


import com.google.auto.service.AutoService;
import com.nextoneday.annotation.BindView;
import com.nextoneday.annotation.OnClick;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/*
 * ===========================================================================================
 * = COPYRIGHT
 *          PAX Computer Technology(Shenzhen) CO., LTD PROPRIETARY INFORMATION
 *   This software is supplied under the terms of a license agreement or nondisclosure
 *   agreement with PAX Computer Technology(Shenzhen) CO., LTD and may not be copied or
 *   disclosed except in accordance with the terms in that agreement.
 *     Copyright (C) 2018-? PAX Computer Technology(Shenzhen) CO., LTD All rights reserved.
 * Description: // Detail description about the function of this module,
 *             // interfaces with the other modules, and dependencies.
 * Revision History:
 * Date	                 Author	                Action
 *  	                   	Create/Add/Modify/Delete
 * ===========================================================================================
 */

/**
 * Author: shah
 * Date : 2020/5/5.
 * Desc : MyCompiler
 */

//用来生成文件，如果没有这个文件就无法触发执行这个编译器
@AutoService(Processor.class)
// 加了这个下面的接口就不用了
@SupportedAnnotationTypes({Constant.BIND_VIEW, Constant.OnClick})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
//接收传参
@SupportedOptions("content")

public class MyCompiler extends AbstractProcessor {

    // 操作element 工具类
    private Elements mElementUtils;
    //用于报告错误
    private Messager mMessager;
    // 文件生成器，用来生成对应java 类
    private Filer mFiler;


    private Map<TypeElement, List<Element>> tempMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mElementUtils = processingEnvironment.getElementUtils();
        mFiler = processingEnvironment.getFiler();

        mMessager = processingEnvironment.getMessager();

        //获取参数
        String content = processingEnvironment.getOptions().get("content");
        mMessager.printMessage(Diagnostic.Kind.NOTE, content);
    }
//
//    /**
//     * 支持的jdk编译版本
//     * @return
//     */
//    @Override
//    public SourceVersion getSupportedSourceVersion() {
//        return super.getSupportedSourceVersion();
//    }
//
//    /**
//     * 支持的注解类型
//     * @return
//     */
//    @Override
//    public Set<String> getSupportedAnnotationTypes() {
//        return super.getSupportedAnnotationTypes();
//    }

    /**
     * 接收从gradle 获取参数
     *
     * @return
     */
    @Override
    public Set<String> getSupportedOptions() {
        return super.getSupportedOptions();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        mMessager.printMessage(Diagnostic.Kind.NOTE, "set ： " + set.toString());
        // 判断一下是否有使用注解，集合里是否有数据
        if (set != null && set.size() > 0) {
            // 获取到所有使用了bindview 注解的元素，然后进行分类存储到集合里面
            Set<? extends Element> bindView = roundEnvironment.getElementsAnnotatedWith(BindView.class);
            Set<? extends Element> onClick = roundEnvironment.getElementsAnnotatedWith(OnClick.class);


            //然后进行分类存储到集合里面
            valueOfMap(bindView);
            valueOfMap(onClick);

            if (tempMap.size() > 0) {

                try {
                    //最后创建java文件
                    createJavaFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }

        return false;
    }

    private void createJavaFile() throws IOException {
        if (tempMap.size() <= 0) {
            return;
        }

        //获取我们定义的接口ViewBinder的元素，
        TypeElement viewBinderType = mElementUtils.getTypeElement(Constant.VIEWBINDER);

//        然后遍历map集合
        Set<TypeElement> typeElements = tempMap.keySet();

        for (TypeElement typeElement : typeElements) {
            // 通过typeElement 获取class name 就是Activity
            ClassName className = ClassName.get(typeElement);

            //创建一个参数化类型 ，传入的是接口名字ViewBinder和 接口对应的泛型,也就是Activity
            //接口泛型
            ParameterizedTypeName typeName = ParameterizedTypeName.get(ClassName.get(viewBinderType),
                    ClassName.get(typeElement));

            //创建参数 就是bind(MainActivity target)
            ParameterSpec parameterSpec = ParameterSpec.builder(ClassName.get(typeElement), Constant.TARGET)
                    .addModifiers(Modifier.FINAL).build();

            //创建方法，传入方法名，修饰符，返回值，以及方法参数
            MethodSpec.Builder bindBuilder = MethodSpec.methodBuilder("bind")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(void.class)
                    .addParameter(parameterSpec);


            //遍历集合，从里面取出，注解对应变量名和 传入的Id
            List<Element> fieldElements = tempMap.get(typeElement);
            for (Element field : fieldElements) {

                ElementKind kind = field.getKind();
                String fieldName = field.getSimpleName().toString();
                if (kind == ElementKind.METHOD) {
                    ExecutableElement method = (ExecutableElement) field;

                    //这是一个匿名内部类
                    ClassName onclickListener = ClassName.get(Constant.ANDROID_VIEW, Constant.VIEW, Constant.ON_CLICK_LISTENER);
                    ClassName viewClassName = ClassName.get(Constant.ANDROID_VIEW, Constant.VIEW);

                    //如果定义的接口有参数就传，如果没有参数就不传
                    MethodSpec.Builder onClick = MethodSpec.methodBuilder(Constant.METHOD_NAME)
                            .addModifiers(Modifier.PUBLIC)
                            .addParameter(viewClassName, Constant.VIEW_NAME)
                            .returns(void.class);
                    List<? extends VariableElement> parameters = method.getParameters();
                    if (parameters.size() > 0) {
                        onClick.addStatement("$N.$N($N)", Constant.TARGET, method.getSimpleName(), Constant.VIEW_NAME);
                    } else {

                        onClick.addStatement("$N.$N()", Constant.TARGET, method.getSimpleName());
                    }


                    TypeSpec listener = TypeSpec.anonymousClassBuilder("")
                            .addSuperinterface(onclickListener)
                            .addMethod(onClick.build())
                            .build();


                    //target.findvViewById().setOnClicklistener();
                    int id = field.getAnnotation(OnClick.class).value();
                    String methodContent = "$N.findViewById($L).setOnClickListener($L)";
                    bindBuilder.addStatement(methodContent, Constant.TARGET, id, listener);

                } else if (kind == ElementKind.FIELD) {

                    int id = field.getAnnotation(BindView.class).value();
                    //然后通过拼接字符串，将方法调用和参数拼接起来,添加到方法里面
                    //target.tv= target.findViewById(R.id.tv);
                    String methodContent = "$N." + fieldName + "= $N.findViewById($L)";
                    bindBuilder.addStatement(methodContent, Constant.TARGET, Constant.TARGET, id);
                }

            }

            //创建一个typespec 也就是类， 类名就是XXActivity$ViewBinder ,添加实现的接口，和修饰符，以及类里面的方法
            TypeSpec bindview = TypeSpec.classBuilder(className.simpleName() + "$ViewBinder")
                    .addSuperinterface(typeName)
                    .addModifiers(Modifier.PUBLIC)
                    .addMethod(bindBuilder.build())
                    .build();


            //最后创建，这个java 文件，文件的包名，就是activity 的包名，

            JavaFile file = JavaFile.builder(className.packageName(), bindview).build();

            //写文件
            file.writeTo(mFiler);


        }

    }


    //因为有很多类，然后很多类里面有很多个变量需要用到，所以需要用map来处理
    private void valueOfMap(Set<? extends Element> elements) {

        if (elements != null && elements.size() > 0) {

            for (Element element : elements) {
                // 遍历元素，获取到父元素，就是MainActivity,
                // 存储为key- list
                // key--Activity   value -- list -- 存放使用注解的那个变量的元素
                TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
                if (tempMap.containsKey(enclosingElement)) {
                    tempMap.get(enclosingElement).add(element);
                } else {
                    List<Element> fields = new ArrayList<>();
                    fields.add(element);
                    tempMap.put(enclosingElement, fields);
                }

            }
        }
    }
}
