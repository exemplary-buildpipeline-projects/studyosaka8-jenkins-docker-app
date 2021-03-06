package com.github.kazuhito_m.odf_edit_sample.workresult.view;

import com.github.kazuhito_m.odf_edit_sample.workresult.entity.WorkresultDay;

public class WorkresultRow extends WorkresultDay {

    public Integer lineNo = 0;

    public WorkresultRow(int lineNo, java.sql.Date resultDate) {
        this.lineNo = lineNo;
        this.resultDate = resultDate;
    }

    public WorkresultRow(int lineNo, WorkresultDay src) {
        this.lineNo = lineNo;
        this.refrect(src);
    }

    public void refrect(WorkresultDay src) {
        this.id = src.id;
        this.userId = src.userId;
        this.resultDate = src.resultDate;
        this.startTime = src.startTime;
        this.endTime = src.endTime;
        this.breakHour = src.breakHour;
        this.description = src.description;
    }

}
