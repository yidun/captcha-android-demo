package com.netease.nis.smartercaptcha.bean;

/**
 * @author liuxiaoshuai
 * @date 2022/6/21
 * @desc
 * @email liulingfeng@mistong.com
 */
public class CustomStyleBean {
    public String gap;
    public String executeBorderRadius;
    public String executeBackground;
    public String executeTop;
    public String executeRight;
    public ImagePanel imagePanel;
    public ControlBar controlBar;

    public static class ImagePanel {
        public String align;
        public String borderRadius;
    }

    public static class ControlBar {
        public String height;
        public String borderRadius;
        public String borderColor;
        public String background;
        public String borderColorMoving;
        public String backgroundMoving;
        public String borderColorSuccess;
        public String backgroundSuccess;
        public String borderColorError;
        public String backgroundError;
        public String slideBackground;
        public String textSize;
        public String textColor;
    }
}
