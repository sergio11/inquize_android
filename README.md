# Inquize - Capture Curiosity. Unlock Knowledge. 🔍📚💡

<img width="auto" height="200px" align="left" src="doc/main_logo.png" />

**Inquize** is a groundbreaking mobile application that redefines how you engage with your surroundings. With the power of the **Gemini SDK**'s advanced AI, Inquize transforms your device into a gateway to knowledge, allowing you to ask questions about any object, place, or concept captured by your camera. Receive precise, real-time answers, turning every moment of curiosity into a learning experience.

Whether you're at a museum 🖼️, exploring nature 🌳, or simply at home 🏠, **Inquize** offers instant access to enriched information, making knowledge discovery as easy as point, ask, and learn! 📱✨

<p align="center">
  <img src="https://img.shields.io/badge/Android%20Studio-3DDC84.svg?style=for-the-badge&logo=android-studio&logoColor=white" />
  <img src="https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white" />
  <img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" />
  <img src="https://img.shields.io/badge/firebase-ffca28?style=for-the-badge&logo=firebase&logoColor=black" />
  <img src="https://img.shields.io/badge/Material%20UI-007FFF?style=for-the-badge&logo=mui&logoColor=white" />
</p>

## 🚀 Key Features

- **AI-Powered Visual Exploration** 🤖📷: Leverage the Gemini SDK to recognize objects, places, and concepts through your camera and obtain instant, accurate information.
- **Natural Language Interaction** 💬: Ask questions in conversational language, and receive detailed, real-time responses that feel natural and engaging.
- **Speech-to-Text and Text-to-Speech Integration** 🗣️🔊: Use your voice to ask questions via Speech-to-Text and listen to answers using Text-to-Speech, making interaction even more seamless.
- **Organized Conversations with Inquizes** 🗂️💬: Each Inquize represents a topic of conversation derived from a photo you've taken. You can ask questions and get answers related to that specific image or topic.
- **Dynamic Learning** 📘🌍: Turn everyday experiences into learning opportunities, discovering facts, history, or technical details about anything around you.
- **Enhanced User Experience** 🖥️✨: Designed with **Jetpack Compose**, the app delivers a seamless, modern UI with fluid animations and an intuitive user flow.

## 🧠 How Inquize Works

- **Firestore** 🔥📂: Used to store "Inquizes"—topics of conversation that the user can engage with. Each Inquize is linked to a specific image and topic, and the user can ask questions to receive detailed answers.
- **Firestorage** 🗄️📸: Stores the images captured by the user, which are analyzed by the Gemini SDK to provide insightful answers about the objects or scenes in the photos.
- **Gemini SDK** 🌟🤖: The AI engine that powers image analysis and natural language understanding, providing detailed answers to user queries based on the images and topics.
- **Speech-to-Text and Text-to-Speech** 🗣️🔄🔊: Allow users to interact with Inquize using their voice, asking questions verbally and listening to answers, enhancing accessibility and user convenience.

## 🔍 Home Screen Features

- **Inquize List** 📄: View all your Inquizes—your ongoing topics of conversation. Each Inquize corresponds to a captured image and its related questions and answers.
- **Search Functionality** 🔎: Easily search through your Inquizes to find specific topics or conversations.
- **Delete Functionality** 🗑️: Manage your Inquizes by deleting any that are no longer needed.
- **Detailed View** 👁️: Access the details of each Inquize, where you can continue the conversation by asking more questions and receiving answers.

## 🛠️ Clean Architecture & MVI for a Robust Foundation

Inquize is built on **Clean Architecture** principles, ensuring that the application is scalable, maintainable, and testable. This architecture promotes a clear separation of concerns, with well-defined layers for data, domain, and presentation, leading to a robust codebase that integrates seamlessly with the **Gemini SDK**.

By using **Model-View-Intent (MVI)** as the UI architectural pattern, Inquize provides a reactive and unidirectional data flow, enhancing the overall user experience. This approach ensures that the app remains responsive, consistent, and easy to extend with new features.

- **Domain-Centric Design** 🧩: The business logic is encapsulated in the domain layer, which interacts with data sources (e.g., repositories) and manages the flow of information to and from the UI.
- **Seamless Integration with Gemini SDK** 🤖🌐: The Clean Architecture approach has enabled smooth integration with the Gemini SDK, ensuring optimal performance in image recognition and natural language processing tasks.

## 🧰 Technologies Used

- **Kotlin** 💻: The primary programming language for Android development.
- **Jetpack Compose** 🖌️: A modern, declarative UI toolkit that allows for flexible and scalable UI design.
- **Gemini SDK** 🚀🤖: The heart of the app's AI capabilities, powering image recognition and natural language understanding.
- **Firestore** 🔥: Manages the storage of Inquizes, providing a structured and scalable way to handle conversation topics.
- **Firestorage** 📸: Stores images that are analyzed by Gemini to generate accurate and detailed responses.
- **Coroutines** ⏱️: Ensures efficient asynchronous task management and smooth user interactions.
- **Hilt/Dagger** 🛠️: Dependency injection frameworks that provide clear module separation and easy scalability.
- **Retrofit** 🌐: Manages network communication with external APIs.
- **Coil** 🎨: Image loading library that seamlessly integrates with Jetpack Compose for high-performance image handling.

## ✨ User Experience Enhancements

- **Optimized Performance** ⚡: Leveraging coroutines for asynchronous operations ensures smooth, lag-free interactions.
- **Intuitive Navigation** 🧭: The UI, built with **Jetpack Compose**, offers a modern, fluid experience that adapts to user input, making the app feel natural and easy to use.
- **Dynamic Responses** 🎯: The combination of the Gemini SDK and MVI architecture provides precise, dynamic answers that evolve with user input, creating a responsive and personalized experience.

## 🔄 Use Cases Covered by Inquize

**Inquize** is designed to be a versatile tool that can be applied across various contexts. Here are some of the key use cases:

### 1. Museum and Gallery Visits 🖼️🏛️
- **Scenario**: You're at a museum or art gallery and come across a painting, sculpture, or artifact. You want to know more about it—its history, artist, or significance.
- **Inquize Solution**: Capture a photo of the item, ask questions, and receive detailed insights instantly. Enhance your experience with rich contextual information, whether it's about the artist's life, the historical context, or the cultural significance of the object.

### 2. Nature Exploration 🌳🦋
- **Scenario**: While hiking or exploring nature, you encounter a plant, animal, or landscape that piques your curiosity.
- **Inquize Solution**: Take a picture and ask questions. **Inquize** can help you identify the species, learn about its habitat, and provide interesting facts. Perfect for nature lovers and curious minds!

### 3. At Home Learning 🏠📚
- **Scenario**: You're at home, and a particular object or concept sparks your interest. Maybe it's a vintage item, a scientific instrument, or something from a DIY project.
- **Inquize Solution**: Capture an image, ask your question, and let **Inquize** guide you with answers that deepen your understanding, whether it's about the item's history, functionality, or potential uses.

### 4. Educational Tool for Students and Teachers 🎓👩‍🏫
- **Scenario**: Students are working on a project or homework and need more information about a topic. Teachers want to provide a more interactive and engaging learning experience.
- **Inquize Solution**: Use **Inquize** as an educational assistant. Students can take pictures of objects or text and ask questions to gain further understanding. Teachers can use it to spark curiosity and engage students with real-world examples.

### 5. Travel Companion 🌍✈️
- **Scenario**: You're traveling to a new city or country and want to learn more about the landmarks, architecture, or local customs.
- **Inquize Solution**: Point your camera at a monument or landmark, and let **Inquize** provide historical facts, cultural insights, and relevant information that enriches your travel experience.

### 6. Accessibility Tool 🦮👨‍🦯
- **Scenario**: For users with visual impairments or disabilities, understanding the world around them through visual information might be challenging.
- **Inquize Solution**: **Inquize** offers a hands-free option through Speech-to-Text and Text-to-Speech, allowing users to ask questions about their surroundings and receive spoken answers, making knowledge more accessible.

### 7. Art and Design Inspiration 🎨🖌️
- **Scenario**: As a designer or artist, you're constantly seeking inspiration. You come across patterns, colors, or designs that spark ideas.
- **Inquize Solution**: Capture images and ask **Inquize** for more information about the styles, trends, or historical significance, helping you build a deeper connection with your creative inspiration.

### 8. Everyday Problem Solving 🛠️🔍
- **Scenario**: You encounter a tool, device, or product at home that you don’t fully understand. Maybe it's a kitchen gadget, a piece of furniture, or a tech device.
- **Inquize Solution**: Use **Inquize** to ask questions about its purpose, how it works, or where it comes from. Gain useful insights to help solve everyday challenges.

### 9. Fun Fact Discovery 🤓🎉
- **Scenario**: You're curious about random objects or concepts in your daily life and want to learn more for fun.
- **Inquize Solution**: Snap a picture, ask a question, and discover fascinating trivia or unknown facts that you can share with friends and family.

### 10. Enhancing Social Experiences 🎤👥
- **Scenario**: During a conversation with friends or at a social event, an interesting topic comes up, and everyone wants to know more about it.
- **Inquize Solution**: Quickly use **Inquize** to provide accurate information on the spot, making you the go-to person for reliable knowledge and keeping the conversation flowing.

**Inquize** is more than just a mobile app; it's a powerful tool for curiosity, education, and discovery, adaptable to a wide range of use cases across different environments. Whether for personal enrichment, professional development, or simply satisfying your curiosity, **Inquize** empowers users to unlock knowledge anytime, anywhere.


 ## Please Share & Star the repository to keep me motivated.
  <a href = "https://github.com/sergio11/vault_keeper_android/stargazers">
     <img src = "https://img.shields.io/github/stars/sergio11/vault_keeper_android" />
  </a>

## License ⚖️

This project is licensed under the MIT License, an open-source software license that allows developers to freely use, copy, modify, and distribute the software. 🛠️ This includes use in both personal and commercial projects, with the only requirement being that the original copyright notice is retained. 📄

Please note the following limitations:

- The software is provided "as is", without any warranties, express or implied. 🚫🛡️
- If you distribute the software, whether in original or modified form, you must include the original copyright notice and license. 📑
- The license allows for commercial use, but you cannot claim ownership over the software itself. 🏷️

The goal of this license is to maximize freedom for developers while maintaining recognition for the original creators.

```
MIT License

Copyright (c) 2024 Dream software - Sergio Sánchez 

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
