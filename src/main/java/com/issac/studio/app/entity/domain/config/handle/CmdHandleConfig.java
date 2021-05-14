package com.issac.studio.app.entity.domain.config.handle;

/**
 * @description: cmd执行handle的配置信息
 * @file: CmdHandleConfig
 * @author: issac.young
 * @date: 2021/5/11 9:11 上午
 * @since: v1.0.0
 * @copyright (C), 1992-2021, issac
 */
public class CmdHandleConfig extends AbstractHandleConfig {
    private String command;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
