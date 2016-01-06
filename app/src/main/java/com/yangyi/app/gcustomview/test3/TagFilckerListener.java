package com.yangyi.app.gcustomview.test3;

import java.util.List;

/**
 * Created by GYH on 2015/12/31.
 */
public abstract interface TagFilckerListener {
    public abstract void startAnimation(List<TagFlickerViewHelp> tagFlickerViewHelps);

    public abstract void resetAnimation(List<TagFlickerViewHelp> tagFlickerViewHelps);

    public abstract void stopAnimation(List<TagFlickerViewHelp> tagFlickerViewHelps);

}
