package com.crt.common.util;

import com.crt.common.vo.ITreeVO;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @Auther: caolu@e6yun.com
 * @Date: 2019/3/1 18:32
 * @Description: 列表转成树形列表通用方法
 */
public class BuildTreeUtil {


    /**
     * 默认情况下传入0 作为 rootId  自定义情况 自行传入
     * @param list
     * @return
     */
    public static List<ITreeVO> listToTree(List<ITreeVO> list){
        return listToTree(list,0);
    }

    /**
     * 将list转成Tree
     * @param list  处理好的list数据
     * @param rootId  指定的跟部门id
     * @return
     */
    public static List<ITreeVO> listToTree(List<ITreeVO> list,Object rootId){
        Multimap<Object,ITreeVO> multimap = LinkedListMultimap.create();
        for (ITreeVO t : list) {
            multimap.put(t.getId(),t);
        }
        return  multimap.values().stream().peek(i->{
            if(multimap.containsKey(i.getPid())){
                Collection<ITreeVO> iTreeVOS = multimap.get(i.getPid());
                for(ITreeVO iTreeVO : iTreeVOS){
                    if(iTreeVO.getChildren() == null){
                        iTreeVO.setChildren(new LinkedList<>());
                    }
                    iTreeVO.getChildren().add(i);
                }
            }
        }).filter(x->x.getPid().equals(rootId)).collect(toList());
    }


}

