export class User {
    eid: number;
    username: string;
    password: string;
    firstName: string;
    lastName: string;
    preferences?: { [key:string]:string; } = {};
}