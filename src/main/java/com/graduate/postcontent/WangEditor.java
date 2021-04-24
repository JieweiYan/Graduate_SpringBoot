
package com.graduate.postcontent;

/**
 * @Description: TODO
 * @author: scott
 * @date: 2021年04月22日 20:41
 */
import lombok.Data;

@Data
public class WangEditor {

    private Integer errno; //错误代码，0 表示没有错误。
    private String data; //已上传的图片路径

    public WangEditor() {
        super();
    }
    public WangEditor(String data) {
        super();
        this.errno = 0;
        this.data = data;
    }
}
