export default class DataUtil {

    public static toDate(time: number): string {
        const date = new Date(time);
        return date.toLocaleDateString();
    }
}
