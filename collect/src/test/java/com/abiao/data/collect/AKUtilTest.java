package com.abiao.data.collect;

import com.abiao.data.util.AKUtil;
import org.junit.Test;

import java.io.IOException;

public class AKUtilTest {

    @Test
    public void akTest() throws IOException {
        System.out.println(AKUtil.get("stock_info_sh_delist", null));
    }
}
