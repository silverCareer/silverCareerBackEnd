package com.example.demo.src.chatroom.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chatroom")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {

    @Id
    @Column(name = "chat_room_idx")
    private Long chatIdx;

    /*
    @Column(name = "sender_idx")
    private Long senderIdx;
    @Column(name = "receiver_idx")
    private Long receiverIdx;
    //senderIdx, receiverIdx 두개 다 auth랑 인데 OneToOne인가..
     */

}
