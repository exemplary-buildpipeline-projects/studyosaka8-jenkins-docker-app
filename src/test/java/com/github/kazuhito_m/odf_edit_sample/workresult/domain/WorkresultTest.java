package com.github.kazuhito_m.odf_edit_sample.workresult.domain;

import com.github.kazuhito_m.odf_edit_sample.Example;
import com.github.kazuhito_m.odf_edit_sample.workresult.view.WorkresultRow;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Example.class})
public class WorkresultTest {

    private static final Logger logger = LoggerFactory.getLogger(WorkresultTest.class);

    @Autowired
    private Workresult sut;

    @Test
    public void ユーザ指定で勤怠の履歴から月のリストを作成取得できる() {
        // 実行
        List<String> actual = sut.getMonths();
        // 検証
        assertThat(actual.isEmpty(), is(false));
        // これ以上過去データは増やさないと思うので…末尾データは最古のもの
        String last = actual.get(actual.size() - 1);
        assertThat(last, is("2014/11"));
    }

    @Test
    public void 指定した範囲の日付のタバを取得できる() {
        // 事前条件
        Date from = new Date(116, 8, 1);
        Date to = new Date(116, 8, 30);
        assertThat(toString(from), is("2016/09/01"));
        assertThat(toString(to), is("2016/09/30"));

        // 実行
        List<Date> days = sut.createDateList(from, to);

        // 検証
        assertThat(days.size(), is(30));
        assertThat(toString(days.get(0)), is("2016/09/01"));
        assertThat(toString(days.get(1)), is("2016/09/02"));
        // ...
        assertThat(toString(days.get(28)), is("2016/09/29"));
        assertThat(toString(days.get(29)), is("2016/09/30"));
    }

    @Test
    public void 指定した範囲の日付をキーとするMapを取得する() {
        // 事前条件
        Date from = new Date(116, 8, 1);
        Date to = new Date(116, 8, 30);
        assertThat(toString(from), is("2016/09/01"));
        assertThat(toString(to), is("2016/09/30"));

        // 実行
        Map<Date, WorkresultRow> actual = sut.createBlankMapBy(from, to);

        // 検証
        assertThat(actual.size(), is(30));
        int i = 1;
        for (WorkresultRow row : actual.values()) {
            if (i == 1) {
                assertThat(row.resultDate, is(from));
            } else if (i == actual.size()) {
                assertThat(row.resultDate, is(to));
            }
            assertThat(row.lineNo, is(i++));
            assertThat(row.resultDate, is(notNullValue()));
            assertThat(row.startTime, is(nullValue()));
        }

    }

    // ユティリティ

    private String toString(Date day) {
        return (new SimpleDateFormat("yyyy/MM/dd")).format(day);
    }

}