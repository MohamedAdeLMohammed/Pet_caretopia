// src/Dashboard/Components/Chat.jsx
function Chat() {
  const token = sessionStorage.getItem("token");

  return (
    <iframe
      src={`https://localhost:8088/connect-chat/connect?token=${token}`}
      style={{ width: '100%', height: '100vh', border: 'none' }}
      title="Chat"
    ></iframe>
  );
}
export default Chat;
