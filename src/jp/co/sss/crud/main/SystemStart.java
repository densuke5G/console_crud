package jp.co.sss.crud.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import jp.co.sss.crud.db.DBController;

/** 社員管理システム実行用クラス */
public class SystemStart {
	/**
	 * メイン処理
	 * @param args	コマンドライン引数
	 */
	public static void main(String[] args) {
		try {
			int menuNo = 0;
			do {
				// メニューの表示
				System.out.println("=== 社員管理システム ===");
				System.out.println("1. 全件表示");
				System.out.println("2. 社員名検索");
				System.out.println("3. 部署ID検索");
				System.out.println("4. 登録");
				System.out.println("5. 更新");
				System.out.println("6. 削除");
				System.out.println("7. 終了");
				System.out.print("メニュー番号を入力してください:");

				// メニュー番号の入力
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				String menuNoStr = br.readLine();
				menuNo = Integer.parseInt(menuNoStr);
				// 整数の入力チェック
				DBController.checkNumber(menuNoStr, 1, 7);

				// 機能の呼出
				switch (menuNo) {
				case 1:
					// 全件表示機能の呼出
					DBController.findAll();
					break;
				case 2:
					String empName;
					do {
						// 検索する値を入力
						System.out.print("社員名を入力してください:");
						empName = br.readLine();
						
						// 文字列の入力チェック
					} while (DBController.checkString(empName, 1, 30));
					
					// 社員名検索機能の呼出
					DBController.findEmpName(empName);
					break;
				case 3:
					String deptId;
					
					do {
						// 検索する値を入力
						System.out.print("部署ID部署ID(1:営業部、2:経理部、3:総務部)を入力してください:");
						deptId = br.readLine();
						// 整数の入力チェック
					} while (DBController.checkNumber(deptId, 1, 3));
					
					// 部署ID検索機能の呼出
					DBController.findDeptId(deptId);
					break;
				case 4:
					String gender;
					String birthday;
					do {
						// 登録する値を入力
						System.out.print("社員名:");
						empName = br.readLine();
						System.out.print("性別(1:男性, 2:女性):");
						gender = br.readLine();
						System.out.print("生年月日(西暦年/月/日):");
						birthday = br.readLine();
						System.out.print("部署ID(1:営業部、2:経理部、3:総務部):");
						deptId = br.readLine();
						// 整数の入力チェック
						// 文字列の入力チェック
						// 日付の入力チェック
					} while (DBController.checkNumber(gender, 1, 2) ||
						DBController.checkNumber(deptId, 1, 3) ||
						DBController.checkString(empName, 1, 30) ||
						DBController.checkDate(birthday));
					
					// 登録機能の呼出
					DBController.insert(empName, gender, birthday, deptId);
					break;
				case 5:
					String  empId;
					do {
						// 更新する値を入力
						System.out.print("更新する社員の社員IDを入力してください:");
						empId = br.readLine();
						System.out.print("社員名:");
						empName = br.readLine();
						System.out.print("性別(1:男性, 2:女性):");
						gender = br.readLine();
						System.out.print("生年月日(西暦年/月/日):");
						birthday = br.readLine();
						System.out.print("部署ID(1:営業部、2:経理部、3:総務部):");
						deptId = br.readLine();
						
						// 更新機能の呼び出し
						DBController.update(empId, empName, gender, birthday, deptId);
						
						// 整数の入力チェック
						// 文字列の入力チェック
						// 日付の入力チェック
					} while (DBController.checkNumber(gender, 1, 2) ||
						DBController.checkNumber(deptId, 1, 3) ||
						DBController.checkString(empName, 1, 30) ||
						DBController.checkDate(birthday));
					
					break;
				case 6:
					do {
						// 削除する値を入力
						System.out.print("削除する社員の社員IDを入力してください:");
						empId = br.readLine();
						
						// 整数の入力チェック
					} while (DBController.checkNumber(empId, 1, 9999));
					
					// 削除機能の呼び出し
					DBController.delete(empId);
					break;
				} 
			} while (menuNo != 7);
		} catch (Exception e) {
			System.out.println("システムエラーが発生しました");
			e.printStackTrace();
		}
		System.out.println("システムを終了します。");
	}
}
