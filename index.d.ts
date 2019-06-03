declare module 'react-native-phone-call-recorder' {
	export function addPhonesWhiteList(phones: string[]): Promise<string[]>;
	export function clearWhiteList(): void;
	export function getWhiteList(): Promise<string[]>;
	export function deletePhoneWhiteList(phone: string): Promise<string[]>;

	export function addPhonesBlackList(phone: string[]): Promise<string[]>;
	export function clearBlackList(): void;
	export function getBlackList(): Promise<string[]>;
	export function deletePhoneBlackList(phone: string): Promise<string[]>;
	export function switchRecordStatus(status: boolean): void;
}
