/*
 *  Copyright Beijing 58 Information Technology Co.,Ltd.
 *
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package com.bj58.spat.gaea.server.bootstrap.serverframe;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.bj58.spat.gaea.server.contract.annotation.OperationContract;
import com.bj58.spat.gaea.server.contract.annotation.ServiceBehavior;
import com.bj58.spat.gaea.server.contract.annotation.ServiceContract;
import com.bj58.spat.gaea.server.contract.log.ILog;
import com.bj58.spat.gaea.server.contract.log.LogFactory;
import com.bj58.spat.gaea.server.deploy.hotdeploy.DynamicClassLoader;
import com.bj58.spat.gaea.server.util.ClassHelper;

/**
 * CheckNode
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class TreeFrame {
	private static ILog logger = LogFactory.getLogger(TreeFrame.class);
	/**
	 * Tree选中记录
	 */
	private static Set<TreePath> checkedPathsSet =  new HashSet<TreePath>();
	
	public static TreeFrame create() {
		return new TreeFrame();
	}
	/**build tree*/
	public JTree buildTree() {
		JTree tree = null;
		/**load file jar and class*/
		DynamicClassLoader classLoader = new DynamicClassLoader();
		try {
			classLoader.addFolder(AssistUtils.getPath());
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("-----------------build Tree Start------------------");
		
		CheckNode parnodes = new CheckNode("method");
		tree = new JTree(parnodes);
		
		List<String> jarList = classLoader.getJarList();
		if(jarList!=null && jarList.size()>0){
			loadClassTreeList(jarList,parnodes,classLoader);
		}
		
		tree.setCellRenderer(new CheckRenderer());
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addMouseListener(new NodeSelectionListener(tree));
		
		logger.info("-----------------build Tree End--------------------");
		return tree;
	}
	
	/**
	 * 根据jar文件中所包含类、方法生成具体树
	 * @param jarList{jar list集合}
	 * @param node 父节点
	 * @param classLoader
	 */
	private static void loadClassTreeList(List<String> jarList,CheckNode node,DynamicClassLoader classLoader){
		for(String ju:jarList){
			if(ju!=null){
				loadClassTreeServiceBehavior(ju,node,classLoader);
			}
		}
	}
	
	/**
	 * 根据jar文件中所包含类、方法生成具体树
	 * @param jarUrl jar路径{ex:e:\a.jar}
	 * @param node node父节点
	 * @param classLoader
	 */
	private static void loadClassTreeServiceContract(String jarUrl,CheckNode node,DynamicClassLoader classLoader){
		try{
			Set<Class<?>> clsSet = ClassHelper.getClassFromJar(jarUrl, classLoader);
			for(Class<?> cls : clsSet) {
				
				//ServiceContract contract = cls.getAnnotation(ServiceContract.class);
				//如果存在ServiceContract的注解则添加
				if(cls.getAnnotation(ServiceContract.class) != null){//定义接口
					CheckNode classnodes = new CheckNode(cls.getName());//类名
					node.add(classnodes);
					Method [] methods = cls.getMethods();/**当前类包含方法*/
					if(methods != null){
						for(Method m : methods){
							if(m.getAnnotation(OperationContract.class)!=null){
								Class<?> methodParameter[] = m.getParameterTypes();/**该方法包含参数*/
								String methodParameterStr = m.getName();
								StringBuffer sbuffer = new StringBuffer();
								if(methodParameter!=null){
									for(int j=0,len=methodParameter.length;j<len;j++){
										sbuffer.append(methodParameter[j].getName());
										if(j < len-1){
											sbuffer.append(",");
										}
									}	
								}
								if(sbuffer!=null){
									methodParameterStr = methodParameterStr+"("+sbuffer.toString()+")";
								}
								CheckNode methodnodes = new CheckNode(methodParameterStr);
								classnodes.add(methodnodes);
								methodnodes = null;
								methodParameterStr = null;
								sbuffer = null;
							}
						}
					}
					classnodes = null;
				}
				
			}
			clsSet = null;
		}catch(Exception e){
			logger.error("根据jar文件中所包含类、方法生成具体树异常!");
		}
	}
	
	
	/**
	 * 根据jar文件中所包含类、方法生成具体树
	 * @param jarUrl jar路径{ex:e:\a.jar}
	 * @param node node父节点
	 * @param classLoader
	 */
	private static void loadClassTreeServiceBehavior(String jarUrl,CheckNode node,DynamicClassLoader classLoader){
		try{
			Set<Class<?>> clsSet = ClassHelper.getClassFromJar(jarUrl, classLoader);
			for(Class<?> cls : clsSet) {
				//如果存在ServiceContract的注解则添加
				if(cls.getAnnotation(ServiceBehavior.class) != null){//定义接口实现类
					CheckNode classnodes = new CheckNode(cls.getName());//类名
					node.add(classnodes);
					
					Method [] methods = cls.getDeclaredMethods();/**当前类包含方法*/
					if(methods != null){
						for(Method m : methods){
							if(Modifier.isPublic(m.getModifiers()) || Modifier.isProtected(m.getModifiers())){
								//if(m.getAnnotation(OperationContract.class)!=null){
									Class<?> methodParameter[] = m.getParameterTypes();/**该方法包含参数*/
									String methodParameterStr = m.getName();
									StringBuffer sbuffer = new StringBuffer();
									if(methodParameter!=null){
										for(int j=0,len=methodParameter.length;j<len;j++){
											String nameStr = methodParameter[j].getName();
											sbuffer.append(nameStr.substring(nameStr.lastIndexOf(".")+1));
											if(j < len-1){
												sbuffer.append(",");
											}
											nameStr = null;
										}	
									}
									if(sbuffer!=null){
										methodParameterStr = methodParameterStr+"("+sbuffer.toString()+")";
									}
									CheckNode methodnodes = new CheckNode(methodParameterStr);
									classnodes.add(methodnodes);
									methodnodes = null;
									methodParameterStr = null;
									sbuffer = null;
								//}
							}
						}
					}
					classnodes = null;
				}
				
			}
			clsSet = null;
		}catch(Exception e){
			logger.error("根据jar文件中所包含类、方法生成具体树异常!");
		}
	}
	
	
	/**
	 * 鼠标点击监听
	 */
	class NodeSelectionListener extends MouseAdapter {
		JTree tree;
		NodeSelectionListener(JTree tree) {
			this.tree = tree;
		}
		public void mouseClicked(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();
			int row = tree.getRowForLocation(x, y);
			TreePath path = tree.getPathForRow(row);			
			if (path != null) {
				CheckNode node = (CheckNode) path.getLastPathComponent();				
				if(node!=null){
					boolean isSelected = !(node.isSelected());
					node.setSelected(isSelected);
					if (isSelected) {
						tree.expandPath(path);
						checkSubTree(path,tree);
					} else {
						tree.collapsePath(path);
						uncheckSubTree(path,tree);
					}
					((DefaultTreeModel) tree.getModel()).nodeChanged(node);
					if (row == 0) {
						tree.revalidate();
						tree.repaint();
					}
				}
			}
		}
	}
	
	/**
	 * 选中树中记录以及子记录
	 * @param path 当前路径
	 * @param tree
	 */
	private static void checkSubTree(final TreePath path,JTree tree) {
		addToCheckedPathsSet(path);
		for (int childIndex = 0; childIndex < getChildrenNumber(path,tree); childIndex++) {
		    TreePath childPath = path.pathByAddingChild(tree.getModel().getChild(path.getLastPathComponent(), childIndex));
		    checkSubTree(childPath,tree);
		}
	}
	/**
	 * 取消树中选中记录以及子记录
	 * @param path 当前路径
	 * @param tree
	 */
	private static void uncheckSubTree(final TreePath path,JTree tree) {
		romeToCheckedPathsSet(path);
		for (int childIndex = 0; childIndex < getChildrenNumber(path,tree); childIndex++) {
		    TreePath childPath = path.pathByAddingChild(tree.getModel().getChild(path.getLastPathComponent(), childIndex));
		    uncheckSubTree(childPath,tree);
		}
	}
	/**
	 * 获得子树数目
	 * @param path
	 * @param tree
	 * @return
	 */
	private static int getChildrenNumber(final TreePath path,final JTree tree){
		Object node = path.getLastPathComponent();
		return tree.getModel().getChildCount(node);
	}
	
	private static void romeToCheckedPathsSet(TreePath path) {
		checkedPathsSet.remove(path);
	}
	private static void addToCheckedPathsSet(TreePath path) {
		checkedPathsSet.add(path);
	}
	/**
	 * 返回选中记录集合
	 * @return
	 */
	public static Set<TreePath> getCheckingPaths(){
		return checkedPathsSet;
	}
}
