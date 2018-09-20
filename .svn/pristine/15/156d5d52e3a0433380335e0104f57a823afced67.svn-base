package com.zdtech.platform.framework.entity;

import com.zdtech.platform.framework.persistence.QueryDef;
import org.springframework.data.domain.Sort;

import javax.persistence.*;

/**
 * Created by leepan on 2016/5/9.
 */
@Entity
@Table(name = "msg_schema_file")
@QueryDef(queryTag = "schema", genericQueryFields = {}, sortFields = {"id"}, direction = Sort.Direction.DESC,
        daoName = "msgSchemaFileDao")
public class MsgSchemaFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String path;

    @Column(name = "file_name")
    private String fileName;

    private String descript;

    public MsgSchemaFile() {
    }

    public MsgSchemaFile(String name, String path, String fileName, String descript) {
        this.name = name;
        this.path = path;
        this.fileName = fileName;
        this.descript = descript;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }
}
