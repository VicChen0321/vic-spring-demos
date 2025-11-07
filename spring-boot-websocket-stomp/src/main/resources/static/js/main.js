'use strict';

let usernamePage = document.querySelector('#username-page');
let chatPage = document.querySelector('#chat-page');
let usernameForm = document.querySelector('#usernameForm');
let messageForm = document.querySelector('#messageForm');
let messageInput = document.querySelector('#message');
let messageArea = document.querySelector('#messageArea');
let connectingElement = document.querySelector('.connecting');

let stompClient = null;
let username = null;

function connect(event) {
  username = document.querySelector('#name').value.trim();

  if (username) {
    usernamePage.classList.add('hidden');
    chatPage.classList.remove('hidden');

    let socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, onConnected, onError);
  }
  event.preventDefault();
}

function onConnected() {
  // 訂閱 public topic
  stompClient.subscribe('/topic/public', onMessageReceived);

  // 通知 server 有新使用者加入
  stompClient.send("/app/addUser",
      {},
      JSON.stringify({ sender: username, type: 'JOIN' })
  );

  connectingElement.classList.add('hidden');
}

function onError(error) {
  connectingElement.textContent = '⚠️ 無法連線至 WebSocket 伺服器，請重新整理重試';
  connectingElement.style.color = 'red';
}

function sendMessage(event) {
  let messageContent = messageInput.value.trim();

  if (messageContent && stompClient) {
    let chatMessage = {
      sender: username,
      content: messageInput.value,
      type: 'CHAT'
    };
    stompClient.send("/app/sendMessage", {}, JSON.stringify(chatMessage));
    messageInput.value = '';
  }
  event.preventDefault();
}

function onMessageReceived(payload) {
  let message = JSON.parse(payload.body);

  let messageElement = document.createElement('li');

  if (message.type === 'JOIN') {
    messageElement.classList.add('event-message');
    message.content = message.sender + ' joined!';
  } else if (message.type === 'LEAVE') {
    messageElement.classList.add('event-message');
    message.content = message.sender + ' left!';
  } else {
    messageElement.classList.add('chat-message');

    let usernameElement = document.createElement('strong');
    let usernameText = document.createTextNode(message.sender);
    usernameElement.appendChild(usernameText);
    messageElement.appendChild(usernameElement);
  }

  let textElement = document.createElement('span');
  let messageText = document.createTextNode(' ' + message.content);
  textElement.appendChild(messageText);

  messageElement.appendChild(textElement);

  messageArea.appendChild(messageElement);
  messageArea.scrollTop = messageArea.scrollHeight;
}

// 綁定事件
usernameForm.addEventListener('submit', connect, true);
messageForm.addEventListener('submit', sendMessage, true);
