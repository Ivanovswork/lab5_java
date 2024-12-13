package num3;

import java.util.LinkedList;
import java.util.Queue;

class Dish {
    private String name;

    public Dish(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

class Order {
    private Dish dish;

    public Order(Dish dish) {
        this.dish = dish;
    }

    public Dish getDish() {
        return dish;
    }
}

class OrderQueue {
    private Queue<Order> orders = new LinkedList<>();
    private int capacity;

    public OrderQueue(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void addOrder(Order order) throws InterruptedException {
        while (orders.size() == capacity) {
            wait(); // ��������, ���� ������� �����
        }
        orders.add(order);
        notifyAll(); // ����������� ������� � ����� ������
    }

    public synchronized Order takeOrder() throws InterruptedException {
        while (orders.isEmpty()) {
            wait(); // ��������, ���� ������� �����
        }
        Order order = orders.poll();
        notifyAll(); // ����������� ���������� � ���, ��� ����� ��� ����
        return order;
    }
}

class Waiter extends Thread {
    private OrderQueue orderQueue;

    public Waiter(OrderQueue orderQueue) {
        this.orderQueue = orderQueue;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; i++) { // ������: 10 �������
                Dish dish = new Dish("����� " + (i + 1));
                Order order = new Order(dish);
                System.out.println("�������� ������ �����: " + dish.getName());
                orderQueue.addOrder(order);
                Thread.sleep(500); // ��������� ����� �� �������� ������
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Chef extends Thread {
    private OrderQueue orderQueue;

    public Chef(OrderQueue orderQueue) {
        this.orderQueue = orderQueue;
    }

    @Override
    public void run() {
        try {
            while (true) { // ����������� ���� ��� ���������� ������ ������
                Order order = orderQueue.takeOrder();
                System.out.println("����� ����� ��������: " + order.getDish().getName());
                Thread.sleep(1000); // ��������� ����� �� ������������� �����
                System.out.println("����� �������� ��������: " + order.getDish().getName());
            }
        } catch (InterruptedException e) {
            System.out.println("");
        }
    }
}

class RestaurantSimulation {

    public static void main(String[] args) {
        int queueCapacity = 5; // ������������ ���������� ������� � �������
        OrderQueue orderQueue = new OrderQueue(queueCapacity);

        Waiter waiter = new Waiter(orderQueue);
        Chef chef1 = new Chef(orderQueue);
        Chef chef2 = new Chef(orderQueue);

        waiter.start();
        chef1.start();
        chef2.start();

        try {
            waiter.join(); // �������� ���������� ������ ���������
            chef1.interrupt(); // ���������� ������ ������ ����� ���������� ������ ���������
            chef2.interrupt(); // ���������� ������ ������ ����� ���������� ������ ���������
            chef1.join();
            chef2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("������ ��������� ���������.");
    }
}