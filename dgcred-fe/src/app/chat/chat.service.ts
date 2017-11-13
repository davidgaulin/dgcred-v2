import { Injectable } from '@angular/core';
import { Chat } from './chat.model';

@Injectable()
export class ChatService {

    constructor() { }

    public chat1: Chat[] = [
        new Chat(
            'right',
            'chat',
            'assets/img/portrait/small/avatar-s-1.png',
            '',
            [
                'How can we help? We are here for you!'
            ]),
        new Chat(
            'left',
            'chat chat-left',
            'assets/img/portrait/small/avatar-s-3.png',
            '1 hour ago',
            [
                'Hey John, I am looking for the best admin template.',
                'Could you please help me to find it out?',
                'It should be angular 4 bootstrap compatible.'
            ]),
        new Chat(
            'right',
            'chat',
            'assets/img/portrait/small/avatar-s-1.png',
            '30 minutes ago',
            [
                'Absolutely!',
                'Apex admin is the responsive angular 4 bootstrap admin template.'
            ]),
        new Chat(
            'left',
            'chat chat-left',
            'assets/img/portrait/small/avatar-s-3.png',
            '',
            [
                'Looks clean and fresh UI.',
                'It is perfect for my next project.',
                'How can I purchase it?'
            ]),
        new Chat(
            'right',
            'chat',
            'assets/img/portrait/small/avatar-s-1.png',
            '',
            [
                'Thanks, from ThemeForest.'                
            ]),
        new Chat(
            'left',
            'chat chat-left',
            'assets/img/portrait/small/avatar-s-3.png',
            '',
            [
                'I will purchase it for sure.',
                'Thanks.'                
            ])
    ];
    public chat2: Chat[] = [
        new Chat(
            'right',
            'chat',
            'assets/img/portrait/small/avatar-s-1.png',
            '',
            [
                'How can we help'
            ]),
        new Chat(
            'left',
            'chat chat-left',
            'assets/img/portrait/small/avatar-s-7.png',
            '1 hours ago',
            [
                'Hi, I spoke with a representative yesterday.',
                'he gave me some steps to fix my problem',
                'but they didn’t help.'
            ]),
        new Chat(
            'right',
            'chat',
            'assets/img/portrait/small/avatar-s-1.png',
            '',
            [
                'I’m sorry to hear that',
                'Can I ask which model of projector you own?',
                'What steps did he suggest you to take?',
                'What sort of issue are you having?'
            ]),
        new Chat(
            'left',
            'chat chat-left',
            'assets/img/portrait/small/avatar-s-7.png',
            '',
            [
                'An issue with the power.'
            ]),
        new Chat(
            'right',
            'chat',
            'assets/img/portrait/small/avatar-s-1.png',
            '',
            [
                'Did you make sure the outlet you plugged it into was functional.'                
            ]),
        new Chat(
            'left',
            'chat chat-left',
            'assets/img/portrait/small/avatar-s-7.png',
            '',
            [
                'Yes'                             
            ])        
    ];
    public chat3: Chat[] = [
        new Chat(
            'right',
            'chat',
            'assets/img/portrait/small/avatar-s-1.png',
            '',
            [
                'How can we help'
            ]),
        new Chat(
            'left',
            'chat chat-left',
            'assets/img/portrait/small/avatar-s-8.png',
            '1 hours ago',
            [
                'Hey John, I am looking for the best admin template.',
                'Could you please help me to find it out?',
                'It should be angular 4 bootstrap compatible.'
            ]),
        new Chat(
            'right',
            'chat',
            'assets/img/portrait/small/avatar-s-1.png',
            '',
            [
                'Absolutely!',
                'Apex admin is the responsive angular 4 bootstrap admin template.'
            ]),
        new Chat(
            'left',
            'chat chat-left',
            'assets/img/portrait/small/avatar-s-8.png',
            '',
            [
                'Looks clean and fresh UI.',
                'It is perfect for my next project.',
                'How can I purchase it?'
            ]),
        new Chat(
            'right',
            'chat',
            'assets/img/portrait/small/avatar-s-1.png',
            '',
            [
                'Thanks, from ThemeForest.'                
            ]),
        new Chat(
            'left',
            'chat chat-left',
            'assets/img/portrait/small/avatar-s-8.png',
            '',
            [
                'I will purchase it for sure.',
                'Thanks.'                
            ])        
    ];
    public chat4: Chat[] = [
        new Chat(
            'right',
            'chat',
            'assets/img/portrait/small/avatar-s-1.png',
            '',
            [
                'How can we help'
            ]),
        new Chat(
            'left',
            'chat chat-left',
            'assets/img/portrait/small/avatar-s-5.png',
            '1 hours ago',
            [
                'Hi, I spoke with a representative yesterday.',
                'he gave me some steps to fix my problem',
                'but they didn’t help.'
            ]),
        new Chat(
            'right',
            'chat',
            'assets/img/portrait/small/avatar-s-1.png',
            '',
            [
                'I’m sorry to hear that',
                'Can I ask which model of projector you own?',
                'What steps did he suggest you to take?',
                'What sort of issue are you having?'
            ]),
        new Chat(
            'left',
            'chat chat-left',
            'assets/img/portrait/small/avatar-s-5.png',
            '',
            [
                'An issue with the power.'
            ]),
        new Chat(
            'right',
            'chat',
            'assets/img/portrait/small/avatar-s-1.png',
            '',
            [
                'Did you make sure the outlet you plugged it into was functional.'                
            ]),
        new Chat(
            'left',
            'chat chat-left',
            'assets/img/portrait/small/avatar-s-5.png',
            '',
            [
                'Yes'                             
            ])        
    ];
    public chat5: Chat[] = [
        new Chat(
            'right',
            'chat',
            'assets/img/portrait/small/avatar-s-1.png',
            '',
            [
                'How can we help'
            ]),
        new Chat(
            'left',
            'chat chat-left',
            'assets/img/portrait/small/avatar-s-9.png',
            '1 hours ago',
            [
                'Hey John, I am looking for the best admin template.',
                'Could you please help me to find it out?',
                'It should be angular 4 bootstrap compatible.'
            ]),
        new Chat(
            'right',
            'chat',
            'assets/img/portrait/small/avatar-s-1.png',
            '',
            [
                'Absolutely!',
                'Apex admin is the responsive angular 4 bootstrap admin template.'
            ]),
        new Chat(
            'left',
            'chat chat-left',
            'assets/img/portrait/small/avatar-s-9.png',
            '',
            [
                'Looks clean and fresh UI.',
                'It is perfect for my next project.',
                'How can I purchase it?'
            ]),
        new Chat(
            'right',
            'chat',
            'assets/img/portrait/small/avatar-s-1.png',
            '',
            [
                'Thanks, from ThemeForest.'                
            ]),
        new Chat(
            'left',
            'chat chat-left',
            'assets/img/portrait/small/avatar-s-9.png',
            '',
            [
                'I will purchase it for sure.',
                'Thanks.'                
            ])        
    ];
    public chat6: Chat[] = [
        new Chat(
            'right',
            'chat',
            'assets/img/portrait/small/avatar-s-1.png',
            '',
            [
                'How can we help'
            ]),
        new Chat(
            'left',
            'chat chat-left',
            'assets/img/portrait/small/avatar-s-4.png',
            '1 hours ago',
            [
                'Hi, I spoke with a representative yesterday.',
                'he gave me some steps to fix my problem',
                'but they didn’t help.'
            ]),
        new Chat(
            'right',
            'chat',
            'assets/img/portrait/small/avatar-s-1.png',
            '',
            [
                'I’m sorry to hear that',
                'Can I ask which model of projector you own?',
                'What steps did he suggest you to take?',
                'What sort of issue are you having?'
            ]),
        new Chat(
            'left',
            'chat chat-left',
            'assets/img/portrait/small/avatar-s-4.png',
            '',
            [
                'An issue with the power.'
            ]),
        new Chat(
            'right',
            'chat',
            'assets/img/portrait/small/avatar-s-1.png',
            '',
            [
                'Did you make sure the outlet you plugged it into was functional.'                
            ]),
        new Chat(
            'left',
            'chat chat-left',
            'assets/img/portrait/small/avatar-s-4.png',
            '',
            [
                'Yes'                             
            ])        
    ];
    public chat7: Chat[] = [
        new Chat(
            'right',
            'chat',
            'assets/img/portrait/small/avatar-s-1.png',
            '',
            [
                'How can we help'
            ]),
        new Chat(
            'left',
            'chat chat-left',
            'assets/img/portrait/small/avatar-s-14.png',
            '1 hours ago',
            [
                'Hey John, I am looking for the best admin template.',
                'Could you please help me to find it out?',
                'It should be angular 4 bootstrap compatible.'
            ]),
        new Chat(
            'right',
            'chat',
            'assets/img/portrait/small/avatar-s-1.png',
            '',
            [
                'Absolutely!',
                'Apex admin is the responsive angular 4 bootstrap admin template.'
            ]),
        new Chat(
            'left',
            'chat chat-left',
            'assets/img/portrait/small/avatar-s-14.png',
            '',
            [
                'Looks clean and fresh UI.',
                'It is perfect for my next project.',
                'How can I purchase it?'
            ]),
        new Chat(
            'right',
            'chat',
            'assets/img/portrait/small/avatar-s-1.png',
            '',
            [
                'Thanks, from ThemeForest.'                
            ]),
        new Chat(
            'left',
            'chat chat-left',
            'assets/img/portrait/small/avatar-s-14.png',
            '',
            [
                'I will purchase it for sure.',
                'Thanks.'                
            ])        
    ];
}