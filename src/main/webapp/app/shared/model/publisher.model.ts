import { IUser } from 'app/core/user/user.model';
import { IPublication } from 'app/shared/model//publication.model';
import { IPublisher } from 'app/shared/model//publisher.model';

export interface IPublisher {
    id?: number;
    user?: IUser;
    publications?: IPublication[];
    follows?: IPublisher[];
    favourites?: IPublication[];
    followers?: IPublisher[];
}

export class Publisher implements IPublisher {
    constructor(
        public id?: number,
        public user?: IUser,
        public publications?: IPublication[],
        public follows?: IPublisher[],
        public favourites?: IPublication[],
        public followers?: IPublisher[]
    ) {}
}
