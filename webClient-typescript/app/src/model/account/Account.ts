import { AccountEnum } from './AccountEnum';

export interface Account {
    name: string|null;
    password: string|null;
    token: string|null;
    role: AccountEnum;
}
