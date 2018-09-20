package com.zdtech.platform.framework.service;

import com.zdtech.platform.framework.entity.Func;
import com.zdtech.platform.framework.entity.Result;
import com.zdtech.platform.framework.entity.SysMenuModel;
import com.zdtech.platform.framework.entity.TreeNodeModel;
import com.zdtech.platform.framework.repository.FuncDao;
import com.zdtech.platform.framework.repository.SysUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leepan on 2016/4/20.
 */
@Service
public class FuncService {
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private FuncDao funcDao;

    public Func getFunc(Long uid){
        return funcDao.findOne(uid);
    }

    public List<Func> getUserFunc(Long userId, Long menuId, Func.Type menuType) {
        if (menuId == null){
            return sysUserDao.findUserRoleFunc(userId,menuType);
        }
        return sysUserDao.findUserRoleFunc(userId,menuId,menuType);
    }
    /*public List<SysMenuModel> getUserMenu(Long userId, Long menuId, Func.Type menuType){
        List<Func> funcs = getUserFunc(userId,menuId,menuType);
        List<SysMenuModel> menus = new ArrayList<>();
        for (Func func:funcs){
            String state = "open";
            List<Func> childs = getUserFunc(userId,func.getId(),menuType);
            if (childs != null && childs.size() > 0){
                state = "closed";
            }
            SysMenuModel smm = new SysMenuModel(func.getId(),func.getName(),func.getIconCls(),func.getUrl(),state,"");
            menus.add(smm);
        }
        return menus;
    }*/
    public List<TreeNodeModel> getUserMenu(Long userId, Long menuId, Func.Type menuType){
        List<Func> funcs = getUserFunc(userId,menuId,menuType);
        List<TreeNodeModel> menus = new ArrayList<>();
        for (Func func:funcs){
            String state = "open";
            List<Func> childs = getUserFunc(userId,func.getId(),menuType);
            if (childs != null && childs.size() > 0){
                state = "closed";
            }
            TreeNodeModel smm = new TreeNodeModel(func.getId().toString(),func.getName(),func.getIconCls(),func.getUrl(),state,"");
            menus.add(smm);
        }
        return menus;
    }
    public List<Func> getAllFuncs(){
        Sort s  = new Sort(Sort.Direction.ASC,"level","seqNo");
        return funcDao.findAll(s);
    }
    public List<Func> getFuncByParent(Long pid) {
        if (pid == null){
            return funcDao.findFirstLevel();
        }else {
            return funcDao.findByParentOrderByLevelAscSeqNoAsc(pid);
        }
    }

    public void delFunc(Long uid) {
        funcDao.delete(uid);
    }

    public Result addFuncSafely(Func func){
        Result result = null;
        boolean exist= false;
        if(func.getId()==null){
            exist = existWithLevel(func.getName(), func.getLevel(), func.getParent());
        }else{
            exist = existWithLevelNotId(func.getParent(),func.getLevel(),func.getName(),func.getId());
        }

        if (exist){
            result = new Result(false,"添加或修改功能时，同层级下已有相同名字的功能!");
        }else{
            funcDao.save(func);
            result = new Result(true,"");
        }
        return result;
    }

    public boolean existWithLevel(String name, Integer level, Long parent){
        Func func = null;
        if (parent==null){
            func = findByName(name,level);
        }else{
            func = findByName(name,level,parent);
        }
        return func!=null ? true:false;
    }

    public Func findByName(String name,Integer level,Long parent){
        return funcDao.findByName(name,level,parent);
    }
    public Func findByName(String name,Integer level){
        return funcDao.findByName(name,level);
    }

    public Func findByName(String name){
        List<Func> funcs = funcDao.findByName(name);
        return funcs!=null && funcs.size()>0 ? funcs.get(0) : null;
    }

    public boolean existWithLevelNotId(Long parent,Integer level,String name,Long id){

        if (parent==null){
            return funcDao.countByNameNotId(level,name,id)>0;
        }else{
            return funcDao.countByNameNotId(parent,level,name,id)>0;
        }
    }

    public List<TreeNodeModel> getFuncTree() {
        List<Func> funcList = getAllFuncs();
        List<TreeNodeModel> tree = new ArrayList<>();
        for (Func func:funcList){
            TreeNodeModel model = new TreeNodeModel(func.getId().toString(),
                    func.getName(),
                    func.getParent() == null?null:func.getParent().toString());
            tree.add(model);
        }
        return buildFuncTree(tree);
    }
    private List<TreeNodeModel> buildFuncTree(List<TreeNodeModel> treeDataList){
        List<TreeNodeModel> newTreeDataList = new ArrayList<>();
        for (TreeNodeModel model : treeDataList) {
            if(model.getParentId() == null) {
                //获取父节点下的子节点
                model.setChildren(getChildrenNode(model.getId(),treeDataList));
                model.setState("closed");
                newTreeDataList.add(model);
            }
        }
        return newTreeDataList;
    }
    private  List<TreeNodeModel> getChildrenNode(String pid , List<TreeNodeModel> treeDataList) {
        List<TreeNodeModel> newTreeDataList = new ArrayList<>();
        for (TreeNodeModel model : treeDataList) {
            if(model.getParentId() == null)  continue;
            //这是一个子节点
            if(model.getParentId().equals(pid)){
                //递归获取子节点下的子节点
                model.setChildren(getChildrenNode(model.getId() , treeDataList));
                newTreeDataList.add(model);
            }
        }
        return newTreeDataList;
    }

}
