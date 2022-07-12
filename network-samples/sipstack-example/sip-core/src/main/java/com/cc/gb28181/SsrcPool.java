package com.cc.gb28181;

/**
 * Ssrc pool
 * wcc 2022/6/23
 */
public interface SsrcPool {
    /**
     * 获取实时播放ssrc
     * @return 0开头字符串
     */
    String generatePlaySsrc();
    /**
     * 获取历史播放ssrc
     * @return 1开头字符串
     */
    String generatePlayBackSsrc();

    /**
     *释放ssrc到pool
     * @param ssrc
     */
    void release(String ssrc);
}
