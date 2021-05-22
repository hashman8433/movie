package com.free.video.model;


import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name="img_file")
public class ImgFile {

    @Id    //主键id
    @GenericGenerator(name = "m-uuid", strategy = "uuid")
    @GeneratedValue(generator = "m-uuid")
    @Column(name = "id")
    private String id;

    @Column(name = "video_file_id") //
    private String videoFileId;

    @Column(name = "file_path") //
    private String filePath;

    @Column(name = "file_path_web") //
    private String filePathWeb;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;
}
