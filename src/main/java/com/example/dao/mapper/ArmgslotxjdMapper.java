package com.example.dao.mapper;

import com.example.dao.entity.Armgslotxjd;
import com.example.dao.entity.ArmgslotxjdExample;
import com.example.dao.entity.ArmgslotxjdKey;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface ArmgslotxjdMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ARMGSLOTXJD
     *
     * @mbg.generated Mon Nov 12 12:15:00 GMT+08:00 2018
     */
    long countByExample(ArmgslotxjdExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ARMGSLOTXJD
     *
     * @mbg.generated Mon Nov 12 12:15:00 GMT+08:00 2018
     */
    int deleteByExample(ArmgslotxjdExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ARMGSLOTXJD
     *
     * @mbg.generated Mon Nov 12 12:15:00 GMT+08:00 2018
     */
    int deleteByPrimaryKey(ArmgslotxjdKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ARMGSLOTXJD
     *
     * @mbg.generated Mon Nov 12 12:15:00 GMT+08:00 2018
     */
    int insert(Armgslotxjd record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ARMGSLOTXJD
     *
     * @mbg.generated Mon Nov 12 12:15:00 GMT+08:00 2018
     */
    int insertSelective(Armgslotxjd record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ARMGSLOTXJD
     *
     * @mbg.generated Mon Nov 12 12:15:00 GMT+08:00 2018
     */
    List<Armgslotxjd> selectByExample(ArmgslotxjdExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ARMGSLOTXJD
     *
     * @mbg.generated Mon Nov 12 12:15:00 GMT+08:00 2018
     */
    Armgslotxjd selectByPrimaryKey(ArmgslotxjdKey key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ARMGSLOTXJD
     *
     * @mbg.generated Mon Nov 12 12:15:00 GMT+08:00 2018
     */
    int updateByExampleSelective(@Param("record") Armgslotxjd record, @Param("example") ArmgslotxjdExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ARMGSLOTXJD
     *
     * @mbg.generated Mon Nov 12 12:15:00 GMT+08:00 2018
     */
    int updateByExample(@Param("record") Armgslotxjd record, @Param("example") ArmgslotxjdExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ARMGSLOTXJD
     *
     * @mbg.generated Mon Nov 12 12:15:00 GMT+08:00 2018
     */
    int updateByPrimaryKeySelective(Armgslotxjd record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ARMGSLOTXJD
     *
     * @mbg.generated Mon Nov 12 12:15:00 GMT+08:00 2018
     */
    int updateByPrimaryKey(Armgslotxjd record);
}