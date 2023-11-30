package jp.co.sss.crud.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

/** データベース操作用クラス */
public class DBController {
	/**
	 * 全件表示
	 * @throws ClassNotFoundException	ドライバクラスが存在しない場合に送出
	 * @throws SQLException	データベース操作時にエラーが発生した場合に送出
	 */
	public static void findAll() throws ClassNotFoundException, SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String genders[] = { "男性", "女性" };

		try {
			// DBに接続
			connection = DBManager.getConnection();

			// ステートメントを作成
			String sql = """
					SELECT emp_id, emp_name, gender, TO_CHAR(birthday, 'YYYY/MM/DD') AS birthday,
						dept_name FROM employee e INNER JOIN department d ON e.dept_id = d.dept_id ORDER BY emp_id ASC""";
			preparedStatement = connection.prepareStatement(sql);

			// SQL文を実行
			resultSet = preparedStatement.executeQuery();

			// レコードを出力
			System.out.println("社員ID\t社員名\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t性別\t生年月日\t部署名");
			while (resultSet.next()) {
				System.out.print(resultSet.getString("emp_id") + "\t\t");
				System.out.print(resultSet.getString("emp_name"));
				for (int i = 0; i < 30 - resultSet.getString("emp_name").length(); i++) {
					System.out.print("  ");
				}
				System.out.print("\t");
				int gender = Integer.parseInt(resultSet.getString("gender"));
				System.out.print(genders[gender - 1] + "\t");
				System.out.print(resultSet.getString("birthday") + "\t");
				System.out.println(resultSet.getString("dept_name"));
			}
			System.out.println("");
		} finally {
			DBManager.close(resultSet);
			DBManager.close(preparedStatement);
			DBManager.close(connection);
		}
	}

	/**
	 * 登録
	 * @param empName	社員名
	 * @param gender	性別
	 * @param birthday	生年月日
	 * @param deptId	部署ID
	 * @throws ClassNotFoundException	ドライバクラスが存在しない場合に送出
	 * @throws SQLException	データベース操作時にエラーが発生した場合に送出
	 */
	public static void insert(String empName, String gender, String birthday, String deptId)
			throws ClassNotFoundException, SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			// DBに接続
			connection = DBManager.getConnection();

			// ステートメントを作成
			String sql = "INSERT INTO employee VALUES(seq_emp.NEXTVAL, ?, ?, ?, ?)";
			preparedStatement = connection.prepareStatement(sql);

			// 入力値をバインド
			preparedStatement.setString(1, empName);
			preparedStatement.setString(2, gender);
			preparedStatement.setString(3, birthday);
			preparedStatement.setString(4, deptId);

			// SQL文を実行
			preparedStatement.executeUpdate();

			// 登録完了メッセージを出力
			System.out.println("社員情報を登録しました");
		} finally {
			DBManager.close(preparedStatement);
			DBManager.close(connection);
		}
	}

	/**
	 * 
	 * @param empId		社員ID
	 * @param empName	社員名
	 * @param gender	性別
	 * @param birthday	生年月日
	 * @param deptId	部署ID
	 * @throws ClassNotFoundException	ドライバクラスが存在しない場合に送出
	 * @throws SQLException	データベース操作時にエラーが発生した場合に送出
	 */
	public static void update(String empId, String empName, String gender, String birthday, String deptId)
			throws ClassNotFoundException, SQLException, ParseException {
		Connection connection = null;
		PreparedStatement preparedStatement1 = null;
		PreparedStatement preparedStatement2 = null;
		PreparedStatement preparedStatement3 = null;
		PreparedStatement preparedStatement4 = null;

		try {
			// DBに接続
			connection = DBManager.getConnection();

			// SQL文を列ごとに分け、データが未入力であった場合更新を実行しないようにする
			// SQLリクエストが増えるので良くないと思っています
			if (empName != "") {
				// ステートメントを作成
				String sql1 = "UPDATE employee SET emp_name = ? WHERE emp_id = ? ";
				preparedStatement1 = connection.prepareStatement(sql1);
				// 入力値をバインド
				preparedStatement1.setString(1, empName);
				preparedStatement1.setString(2, empId);
				// 文字列の入力チェック
				checkString(empName, 1, 30);

				// SQL文を実行
				preparedStatement1.executeUpdate();
			}

			if (gender != "") {
				// ステートメントを作成
				String sql2 = "UPDATE employee SET gender = ? WHERE emp_id = ? ";
				preparedStatement2 = connection.prepareStatement(sql2);
				// 入力値をバインド
				preparedStatement2.setString(1, gender);
				preparedStatement2.setString(2, empId);
				// 整数の入力チェック
				DBController.checkNumber(gender, 1, 2);

				// SQL文を実行
				preparedStatement2.executeUpdate();
			}

			if (birthday != "") {
				// ステートメントを作成
				String sql3 = "UPDATE employee SET birthday = ? WHERE emp_id = ? ";
				preparedStatement3 = connection.prepareStatement(sql3);
				// 入力値をバインド
				preparedStatement3.setString(1, birthday);
				preparedStatement3.setString(2, empId);
				// 日付の入力チェック
				DBController.checkDate(birthday);

				// SQL文を実行
				preparedStatement3.executeUpdate();
			}

			if (deptId != "") {
				// ステートメントを作成
				String sql4 = "UPDATE employee SET dept_id = ? WHERE emp_id = ? ";
				preparedStatement4 = connection.prepareStatement(sql4);
				// 入力値をバインド
				preparedStatement4.setString(1, deptId);
				preparedStatement4.setString(2, empId);
				// 整数の入力チェック
				DBController.checkNumber(deptId, 1, 3);

				// SQL文を実行
				preparedStatement4.executeUpdate();
			}

			// 登録完了メッセージを出力
			System.out.println("社員情報を更新しました");
		} finally {
			DBManager.close(preparedStatement1);
			DBManager.close(preparedStatement2);
			DBManager.close(preparedStatement3);
			DBManager.close(preparedStatement4);
			DBManager.close(connection);
		}
	}

	/**
	 * 
	 * @param empId		社員ID
	 * @throws ClassNotFoundException	ドライバクラスが存在しない場合に送出
	 * @throws SQLException	データベース操作時にエラーが発生した場合に送出
	 */
	public static void delete(String empId) throws ClassNotFoundException, SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			// DBに接続
			connection = DBManager.getConnection();

			// ステートメントを作成
			String sql = "DELETE FROM employee WHERE emp_id = ? ";
			preparedStatement = connection.prepareStatement(sql);

			// 入力値をバインド
			preparedStatement.setString(1, empId);

			// SQL文を実行
			preparedStatement.executeUpdate();

			// 登録完了メッセージを出力
			System.out.println("社員情報を削除しました");
		} finally {
			DBManager.close(preparedStatement);
			DBManager.close(connection);
		}
	}

	/**
	 * 
	 * @param empName		社員名
	 * @throws ClassNotFoundException	ドライバクラスが存在しない場合に送出
	 * @throws SQLException	データベース操作時にエラーが発生した場合に送出
	 */
	public static void findEmpName(String empName) throws ClassNotFoundException, SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String genders[] = { "男性", "女性" };

		try {
			// DBに接続
			connection = DBManager.getConnection();

			// ステートメントを作成
			String sql = """
					SELECT emp_id, emp_name, gender, TO_CHAR(birthday, 'YYYY/MM/DD') AS birthday,
						dept_name FROM employee e INNER JOIN department d ON e.dept_id = d.dept_id
						WHERE emp_name LIKE ? ORDER BY emp_id ASC""";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, "%" + empName + "%");

			// SQL文を実行
			resultSet = preparedStatement.executeQuery();

			// レコードを出力
			System.out.println("社員ID\t社員名\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t性別\t生年月日\t部署名");
			int count = 0;
			while (resultSet.next()) {
				System.out.print(resultSet.getString("emp_id") + "\t");
				System.out.print(resultSet.getString("emp_name"));
				for (int i = 0; i < 30 - resultSet.getString("emp_name").length(); i++) {
					System.out.print("  ");
				}
				System.out.print("\t");
				int gender = Integer.parseInt(resultSet.getString("gender"));
				System.out.print(genders[gender - 1] + "\t");
				System.out.print(resultSet.getString("birthday") + "\t");
				System.out.println(resultSet.getString("dept_name"));
				count++;
			}
			if (count == 0) {
				System.out.println("該当する社員は存在しません");
			}
			System.out.println("");
		} finally {
			DBManager.close(resultSet);
			DBManager.close(preparedStatement);
			DBManager.close(connection);
		}
	}

	/**
	 * 
	 * @param deptId		部署ID
	 * @throws ClassNotFoundException	ドライバクラスが存在しない場合に送出
	 * @throws SQLException	データベース操作時にエラーが発生した場合に送出
	 */
	public static void findDeptId(String deptId) throws ClassNotFoundException, SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String genders[] = { "男性", "女性" };

		try {
			// DBに接続
			connection = DBManager.getConnection();

			// ステートメントを作成
			String sql = """
					SELECT emp_id, emp_name, gender, TO_CHAR(birthday, 'YYYY/MM/DD') AS birthday,
						dept_name FROM employee e INNER JOIN department d ON e.dept_id = d.dept_id
						WHERE e.dept_id LIKE ? ORDER BY emp_id ASC""";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, "%" + deptId + "%");

			// SQL文を実行
			resultSet = preparedStatement.executeQuery();

			// レコードを出力
			System.out.println("社員ID\t社員名\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t性別\t生年月日\t部署名");
			int count = 0;
			while (resultSet.next()) {
				System.out.print(resultSet.getString("emp_id") + "\t");
				System.out.print(resultSet.getString("emp_name"));
				for (int i = 0; i < 30 - resultSet.getString("emp_name").length(); i++) {
					System.out.print("  ");
				}
				System.out.print("\t");
				int gender = Integer.parseInt(resultSet.getString("gender"));
				System.out.print(genders[gender - 1] + "\t");
				System.out.print(resultSet.getString("birthday") + "\t");
				System.out.println(resultSet.getString("dept_name"));
				count++;
			}
			if (count == 0) {
				System.out.println("該当する社員は存在しません");
			}
			System.out.println("");
		} finally {
			DBManager.close(resultSet);
			DBManager.close(preparedStatement);
			DBManager.close(connection);
		}

	}

	/**
	 * 
	 * @param input		readline()で入力された値
	 * @param min		最小値
	 * @param max		最大値
	 * @return			trueならループ継続
	 */
	public static boolean checkNumber(String input, int min, int max) {
		if (input == "") {
			return false;
		}
		// 文字列の入力を排除
		Pattern pattern = Pattern.compile("^[0-9999]$");
		if (pattern.matcher(input).matches() == false) {
			System.out.println(min + "以上" + max + "以下の整数を入力してください");
			return true;
		}
		// 数字を範囲指定し入力チェック
		if (Integer.parseInt(input) > max || Integer.parseInt(input) < min) {
			System.out.println(min + "以上" + max + "以下の整数を入力してください");
			return true;
		}
		return false;

	}

	/**
	 * 
	 * @param input		readline()で入力された値
	 * @param min		最小値
	 * @param max		最大値
	 * @return			trueならループ継続
	 */
	public static boolean checkString(String input, int min, int max) {
		if (input == "") {
			return false;
		}
		// 数字の入力を排除
		Pattern pattern = Pattern.compile("^[ぁ-んァ-ヶｱ-ﾝﾞﾟ一-龠]*$");
		if (pattern.matcher(input).matches() == false) {
			System.out.println(min + "文字以上" + max + "文字以下の文字列を入力してください");
			return true;
		}
		// 文字列を文字数で入力チェック
		if (input.length() > max || input.length() < min) {
			System.out.println(min + "文字以上" + max + "文字以下の文字列を入力してください");
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param input		readline()で入力された値
	 * @return			trueならループ継続
	 * @throws ParseException
	 */
	public static boolean checkDate(String input) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		dateFormat.setLenient(false);

		if (input == "") {
			return false;
		}

		// 日付を正しい形式で入力チェック
		try {
			dateFormat.parse(input);
		} catch (ParseException e) {
			System.out.println("正しい形式(西暦年/月/日)で日付を入力してください：");
			return true;
		}
		return false;
	}

}
