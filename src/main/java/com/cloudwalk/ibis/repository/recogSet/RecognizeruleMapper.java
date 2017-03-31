package com.cloudwalk.ibis.repository.recogSet;

import java.util.List;
import java.util.Map;

import com.cloudwalk.ibis.model.recogSet.Recognizerule;

public interface RecognizeruleMapper {

    int deleteByPrimaryKey(String id);

    int insert(Recognizerule record);

    Recognizerule selectByPrimaryKey(String id);

    int updateByPrimaryKey(Recognizerule record);
    
    List<Recognizerule> selectByUser(String userid);
    
    /**
     * 根据策略ID和引擎代码查询识别规则信息
     * @param record
     * @return
     */
    public List<Recognizerule> selectRecogsetpRule(Recognizerule record);
    
    
    public List<Recognizerule> selectRecognizerulePage(Map<String,Object> map);
}