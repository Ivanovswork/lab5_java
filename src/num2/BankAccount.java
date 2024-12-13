package num2;

class BankAccount {
    private double balance;

    public BankAccount(double initialBalance) {
        this.balance = initialBalance;
    }

    public synchronized boolean transfer(BankAccount target, double amount) {
        if (amount <= 0) return false;


        if (balance >= amount) {
            balance -= amount;
            target.receive(amount);
            return true;
        } else {
            System.out.println("Недостаточно средств для перевода. Баланс: " + balance);
            return false;
        }
    }

    private synchronized void receive(double amount) {
        balance += amount;
    }

    public synchronized double getBalance() {
        return balance;
    }
}

class Client extends Thread {
    private static final int MAX_TRANSACTIONS = 10;
    private static final long TRANSACTION_TIMEOUT = 1000;

    private final BankAccount[] accounts;

    public Client(BankAccount[] accounts) {
        this.accounts = accounts;
    }

    @Override
    public void run() {
        int transactionCount = 0;

        while (transactionCount < MAX_TRANSACTIONS) {
            int fromIndex = (int) (Math.random() * accounts.length);
            int toIndex = (int) (Math.random() * accounts.length);
            double amount = Math.random() * 100;

            if (fromIndex != toIndex) {
                boolean success = accounts[fromIndex].transfer(accounts[toIndex], amount);
                if (success) {
                    transactionCount++;
                    System.out.println("Переведено " + amount + " с счета " + fromIndex + " на счет " + toIndex);
                }
            }

            try {
                Thread.sleep((long) (Math.random() * 500));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}

class BankSimulation {
    public static void main(String[] args) throws InterruptedException {
        int numberOfAccounts = 5;
        BankAccount[] accounts = new BankAccount[numberOfAccounts];

        for (int i = 0; i < numberOfAccounts; i++) {
            accounts[i] = new BankAccount(1000);
        }

        Client[] clients = new Client[10];
        for (int i = 0; i < clients.length; i++) {
            clients[i] = new Client(accounts);
            clients[i].start();
        }

        for (Client client : clients) {
            client.join();
        }

        for (int i = 0; i < numberOfAccounts; i++) {
            System.out.println("Баланс счета " + i + ": " + accounts[i].getBalance());
        }
    }
}