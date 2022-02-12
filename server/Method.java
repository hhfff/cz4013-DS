public enum Method {
    CREATE_ACCOUNT(1),
    CLOSE_ACCOUNT(2),
    DEPOSITE(3),
    WITHDRAW(4),
    CURRENCY_EXCHANGE(5),
    VIEW_BALANCE(6),
    MONITOR(7);
    private int value;
    private Method(int value){
        this.value=value;
    }
    public int getValue(){
        return value;
    }

}
