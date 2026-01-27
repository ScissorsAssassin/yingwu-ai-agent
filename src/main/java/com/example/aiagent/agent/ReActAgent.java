package com.example.aiagent.agent;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class ReActAgent extends BaseAgent {

    /**
     * 处理当前状态并且进行下一步行动
     *
     * @return true表示需要执行下一步行动，false表示不需要执行下一步行动
     */
    public abstract boolean think();

    /**
     * 执行绝对的行动
     *
     * @return
     */
    public abstract String act();

    /**
     * 执行单个步骤：思考和行动
     *
     * @return
     */
    @Override
    public String step() {
        try {
            // 判断是否需要执行
            boolean shouldThink = think();
            if (!shouldThink) {
                return "Thinking complete - no action needed";
            }
            return act();
        } catch (Exception e) {
            return "Error Think or Act: " + e.getMessage();
        }
    }
}