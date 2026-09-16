import request from '@/utils/request'

// Send AI chat message and get response
export function sendAiMessage(data) {
  return request.post('/ai/chat', data)
}

// Get chat history
export function getChatHistory() {
  return request.get('/ai/history')
}

// Clear chat history
export function clearChatHistory() {
  return request.delete('/ai/history')
}
