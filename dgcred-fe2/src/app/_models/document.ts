import { User } from '../_models/index';
export class Document {

	constructor(
    public eid: number,
    public contentType: string,
    public fileName: string) { }
}