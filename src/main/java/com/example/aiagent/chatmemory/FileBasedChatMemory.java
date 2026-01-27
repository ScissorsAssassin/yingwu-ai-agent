package com.example.aiagent.chatmemory;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class FileBasedChatMemory implements ChatMemory {
    private final String BASE_PATH;
    private final ReentrantLock lock = new ReentrantLock();
    private static final Kryo kryo = new Kryo();

    static {
        kryo.setRegistrationRequired(false);
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
        // 注册可能用到的类（根据实际Message实现调整）
        kryo.register(ArrayList.class);
        kryo.register(Message.class);
    }

    public FileBasedChatMemory(String dir) throws IOException {
        this.BASE_PATH = dir;
        Path path = Path.of(this.BASE_PATH);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }

    private File getConversationFile(String conversationId) {
        return Path.of(BASE_PATH, conversationId + ".kryo").toFile();
    }

    @Override
    public void add(String conversationId, List<Message> messages) {
        if (messages == null || messages.isEmpty()) return;

        lock.lock();
        try {
            File file = getConversationFile(conversationId);
            List<Message> existingMessages = file.exists() ? get(conversationId) : new ArrayList<>();//条件 ? 表达式1 : 表达式2
            existingMessages.addAll(messages);

            try (Output output = new Output(new FileOutputStream(file))) {
                kryo.writeObject(output, existingMessages);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to save conversation: " + conversationId, e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<Message> get(String conversationId) {
        File file = getConversationFile(conversationId);
        if (!file.exists()) return Collections.emptyList();

        lock.lock();
        try (Input input = new Input(new FileInputStream(file))) {
            return kryo.readObject(input, ArrayList.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load conversation: " + conversationId, e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void clear(String conversationId) {
        lock.lock();
        try {
            Files.deleteIfExists(getConversationFile(conversationId).toPath());
        } catch (IOException e) {
            throw new RuntimeException("Failed to clear conversation: " + conversationId, e);
        } finally {
            lock.unlock();
        }
    }
}
