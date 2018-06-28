public class Main {
    public static void main(String args[]) {
        if (args.length != 4) System.out.println("Использование: java Main <url подключения к БД (пример: \"jdbc:h2:~/test\")> <пользователь (\"sa\")> <пароль (\"\")> <количество>");
        DBService DBService = new DBService();
        DBService.setUrl(args[0]);
        DBService.setUser(args[1]);
        DBService.setPassword(args[2]);
        DBService.setNum(Integer.parseInt(args[3]));
        DBService.setConnection();
        DBService.fillInTable();
        XMLService.createXML(DBService);
        DBService.closeConnection();
        XMLService.transformXML();
        System.out.println("Арифметическая сумма значений = " + XMLService.parseXML());
    }
}
