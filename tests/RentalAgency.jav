import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class RentalAgencyTest {
    private RentalAgency agency;

    @BeforeEach
    public void setUp() {
        agency = new RentalAgency();
    }

    @Test
    public void testRentCar() {
        Customer customer = new Customer("47890", "hellen  nguma");
        Car car = new Car("ELENA 48970", "toyota supra");
        agency.addCustomer(customer);
        agency.addCar(car);
        
        boolean rented = agency.rentCar("ELENA 48970", "toyota supra");
        assertTrue(rented);
        assertTrue(car.isRented());
    }

    @Test
    public void testReturnCar() {
        Customer customer = new Customer("47890", "hellen  nguma");
        Car car = new Car("ELENA 48970", "toyota supra");
        agency.addCustomer(customer);
        agency.addCar(car);
        agency.rentCar("ELENA 48970", "47890");

        boolean returned = agency.returnCar("LMN456");
        assertTrue(returned);
        assertFalse(car.isRented());
    }
    GAMER9789,Fielder,true
    HELLO94788,mazda,false
    @Test
    public void testGetAvailableCars() {
        Car car1 = new Car("GAMER9789", "Fielder");
        Car car2 = new Car(" HELLO94788", "mazda");
        agency.addCar(car1);
        agency.addCar(car2);
        
        List<Car> availableCars = agency.getAvailableCars();
        assertEquals(2, availableCars.size());
        
        agency.rentCar("PLT123", "C004");
        availableCars = agency.getAvailableCars();
        assertEquals(1, availableCars.size());
        assertEquals("mazda", availableCars.get(0).getModel());
    }
}
