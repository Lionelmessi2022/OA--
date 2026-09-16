<template>
  <div class="chat-container">
    <div class="chat-header">
      <h2>AI 智能对话助手</h2>
      <el-button type="danger" size="small" icon="Delete" @click="handleClear" :disabled="!messages.length">
        清空记录
      </el-button>
    </div>

    <div class="chat-messages" ref="messagesRef">
      <div v-if="!messages.length" class="empty-chat">
        <el-icon :size="64" color="#c0c4cc"><ChatDotRound /></el-icon>
        <p>开始与 AI 助手对话吧！</p>
        <div class="quick-questions">
          <el-button v-for="q in quickQuestions" :key="q" @click="sendQuick(q)" size="small" round>
            {{ q }}
          </el-button>
        </div>
      </div>

      <div v-for="(msg, idx) in messages" :key="idx" :class="['message-row', msg.role]">
        <el-avatar :size="36" :class="msg.role">
          {{ msg.role === 'user' ? (userStore.userInfo?.name?.charAt(0) || '我') : 'AI' }}
        </el-avatar>
        <div class="message-bubble">
          <div class="message-content">{{ msg.content }}</div>
          <div class="message-time">{{ msg.time }}</div>
        </div>
      </div>

      <div v-if="aiLoading" class="message-row assistant">
        <el-avatar :size="36" class="assistant">AI</el-avatar>
        <div class="message-bubble">
          <div class="typing-indicator">
            <span></span><span></span><span></span>
          </div>
        </div>
      </div>
    </div>

    <div class="chat-input">
      <el-input
        v-model="inputText"
        type="textarea"
        :rows="3"
        placeholder="输入你的问题..."
        resize="none"
        @keydown.enter.exact.prevent="handleSend"
      />
      <el-button type="primary" icon="Promotion" :loading="aiLoading" :disabled="!inputText.trim()" @click="handleSend">
        发送
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, nextTick } from 'vue'
import { useUserStore } from '@/stores/user'
import { sendAiMessage, clearChatHistory } from '@/api/ai'
import { ElMessage } from 'element-plus'
import dayjs from 'dayjs'

const userStore = useUserStore()
const messagesRef = ref()
const messages = ref([])
const inputText = ref('')
const aiLoading = ref(false)

const quickQuestions = [
  '帮我分析本月考勤数据',
  '生成本周工作周报',
  '查询公司请假制度',
  '如何提升工作效率'
]

function getTime() {
  return dayjs().format('HH:mm')
}

function scrollToBottom() {
  nextTick(() => {
    if (messagesRef.value) {
      messagesRef.value.scrollTop = messagesRef.value.scrollHeight
    }
  })
}

function sendQuick(q) {
  inputText.value = q
  handleSend()
}

async function handleSend() {
  const text = inputText.value.trim()
  if (!text || aiLoading.value) return

  messages.value.push({ role: 'user', content: text, time: getTime() })
  inputText.value = ''
  aiLoading.value = true
  scrollToBottom()

  try {
    const res = await sendAiMessage({ message: text })
    const aiReply = res.data?.reply || res.data || '抱歉，暂时无法回复。'
    messages.value.push({ role: 'assistant', content: aiReply, time: getTime() })
  } catch (e) {
    messages.value.push({ role: 'assistant', content: '网络异常，请稍后重试。', time: getTime() })
  } finally {
    aiLoading.value = false
    scrollToBottom()
  }
}

async function handleClear() {
  try {
    await clearChatHistory()
  } catch (e) {}
  messages.value = []
  ElMessage.success('聊天记录已清空')
}
</script>

<style scoped>
.chat-container {
  display: flex;
  flex-direction: column;
  height: calc(100vh - 100px);
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
}

.chat-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  border-bottom: 1px solid #ebeef5;
}

.chat-header h2 {
  font-size: 18px;
  color: #303133;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
}

.empty-chat {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: #909399;
}

.empty-chat p {
  margin: 16px 0;
  font-size: 16px;
}

.quick-questions {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  justify-content: center;
  margin-top: 8px;
}

.message-row {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
  max-width: 75%;
}

.message-row.user {
  flex-direction: row-reverse;
  margin-left: auto;
}

.message-row.user .el-avatar {
  background: #409eff;
  color: #fff;
  flex-shrink: 0;
}

.message-row.assistant .el-avatar {
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: #fff;
  flex-shrink: 0;
}

.message-bubble {
  background: #f4f4f5;
  padding: 12px 16px;
  border-radius: 12px;
  max-width: 100%;
}

.message-row.user .message-bubble {
  background: #409eff;
  color: #fff;
  border-top-right-radius: 4px;
}

.message-row.assistant .message-bubble {
  background: #f4f4f5;
  color: #303133;
  border-top-left-radius: 4px;
}

.message-content {
  font-size: 14px;
  line-height: 1.6;
  white-space: pre-wrap;
  word-break: break-word;
}

.message-time {
  font-size: 11px;
  margin-top: 4px;
  opacity: 0.6;
}

.typing-indicator {
  display: flex;
  gap: 4px;
  padding: 4px 0;
}

.typing-indicator span {
  width: 8px;
  height: 8px;
  background: #c0c4cc;
  border-radius: 50%;
  animation: typing 1.4s infinite;
}

.typing-indicator span:nth-child(2) { animation-delay: 0.2s; }
.typing-indicator span:nth-child(3) { animation-delay: 0.4s; }

@keyframes typing {
  0%, 60%, 100% { transform: translateY(0); opacity: 0.4; }
  30% { transform: translateY(-8px); opacity: 1; }
}

.chat-input {
  display: flex;
  gap: 12px;
  padding: 16px 20px;
  border-top: 1px solid #ebeef5;
  align-items: flex-end;
}

.chat-input .el-textarea {
  flex: 1;
}
</style>