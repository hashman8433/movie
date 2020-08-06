package com.free.video.model;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="tag_file")
public class TagFile {


    @Id    //主键id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//主键生成策略
    @Column(name = "id")//数据库字段名
    private String id;
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tag_file.tag_id
     *
     * @mbggenerated Tue Aug 04 23:32:40 CST 2020
     */
    @Column(name = "tag_id")
    private String tagId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tag_file.file_id
     *
     * @mbggenerated Tue Aug 04 23:32:40 CST 2020
     */
    @Column(name = "file_id")
    private String fileId;
}
