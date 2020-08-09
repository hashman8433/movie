package com.free.video.model;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.*;
import java.util.Date;


@Data
@Entity
@ToString
@Table(name="video_file")
public class VideoFile {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column video_file.id
     *
     * @mbggenerated Tue Aug 04 23:32:40 CST 2020
     */
    @Id    //主键id
    @GenericGenerator(name = "m-uuid", strategy = "uuid")
    @GeneratedValue(generator = "m-uuid")
    @Column(name = "id")
    private String id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column video_file.name
     *
     * @mbggenerated Tue Aug 04 23:32:40 CST 2020
     */
    @Column(name = "title")
    private String title;


    @Column(name = "fileName")
    private String fileName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column video_file.file_path
     *
     * @mbggenerated Tue Aug 04 23:32:40 CST 2020
     */
    @Column(name = "file_path")
    private String filePath;

    // 页面访问的相对路径
    @Column(name = "file_path_web")
    private String filePathWeb;


    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column video_file.file_size
     *
     * @mbggenerated Tue Aug 04 23:32:40 CST 2020
     */
    @Column(name = "fileSize")
    private String fileSize;

    @Column(name = "scan_status")
    private String scanStatus;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column video_file.update_time
     *
     * @mbggenerated Tue Aug 04 23:32:40 CST 2020
     */
    @Column(name = "updateTime")
    private Date updateTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column video_file.create_time
     *
     * @mbggenerated Tue Aug 04 23:32:40 CST 2020
     */
    @Column(name = "createTime")
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column video_file.is_delete
     *
     * @mbggenerated Tue Aug 04 23:32:40 CST 2020
     */
    @Column(name = "isDelete")
    private String isDelete;
}