import { IUser } from 'app/core/user/user.model';
import { IPublication } from 'app/shared/model//publication.model';
import { IPublisher } from 'app/shared/model//publisher.model';

export interface IPublisher {
    id?: number;
    is?: IUser;
    publications?: IPublication[];
    mentionedBies?: IPublication[];
    followedBies?: IPublisher[];
    favs?: IPublication[];
    likes?: IPublication[];
    follows?: IPublisher[];
}

export class Publisher implements IPublisher {
    constructor(
        public id?: number,
        public is?: IUser,
        public publications?: IPublication[],
        public mentionedBies?: IPublication[],
        public followedBies?: IPublisher[],
        public favs?: IPublication[],
        public likes?: IPublication[],
        public follows?: IPublisher[]
    ) {}
}
