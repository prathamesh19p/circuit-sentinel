package com.p.circuitbreaker.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TravelDestination Model Tests")
class TravelDestinationTest {

    @Test
    @DisplayName("Should create TravelDestination with builder pattern")
    void shouldCreateTravelDestinationWithBuilder() {
        TravelDestination destination = TravelDestination.builder()
                .destinationId("DEST001")
                .country("USA")
                .name("Rocky Mountain National Park")
                .city("Estes Park")
                .description("A beautiful national park with stunning mountain views")
                .category("National Park")
                .bestSeasonToVisit("Summer")
                .attractions("Trail Ridge Road, Bear Lake, Alpine Visitor Center")
                .build();

        assertNotNull(destination);
        assertEquals("DEST001", destination.getDestinationId());
        assertEquals("USA", destination.getCountry());
        assertEquals("Rocky Mountain National Park", destination.getName());
        assertEquals("Estes Park", destination.getCity());
        assertEquals("A beautiful national park with stunning mountain views", destination.getDescription());
        assertEquals("National Park", destination.getCategory());
        assertEquals("Summer", destination.getBestSeasonToVisit());
        assertEquals("Trail Ridge Road, Bear Lake, Alpine Visitor Center", destination.getAttractions());
    }

    @Test
    @DisplayName("Should create TravelDestination with constructor")
    void shouldCreateTravelDestinationWithConstructor() {
        TravelDestination destination = new TravelDestination(
                "DEST002", "Canada", "Banff National Park", "Banff",
                "Stunning Canadian Rockies", "National Park", "Summer",
                "Lake Louise, Moraine Lake, Icefields Parkway"
        );

        assertNotNull(destination);
        assertEquals("DEST002", destination.getDestinationId());
        assertEquals("Canada", destination.getCountry());
        assertEquals("Banff National Park", destination.getName());
    }

    @Test
    @DisplayName("Should create empty TravelDestination")
    void shouldCreateEmptyTravelDestination() {
        TravelDestination destination = new TravelDestination();
        
        assertNotNull(destination);
        assertNull(destination.getDestinationId());
        assertNull(destination.getCountry());
        assertNull(destination.getName());
    }

    @Test
    @DisplayName("Should set and get properties correctly")
    void shouldSetAndGetProperties() {
        TravelDestination destination = new TravelDestination();
        
        destination.setDestinationId("DEST003");
        destination.setCountry("France");
        destination.setName("Eiffel Tower");
        destination.setCity("Paris");
        destination.setDescription("Iconic iron lattice tower");
        destination.setCategory("Landmark");
        destination.setBestSeasonToVisit("Spring");
        destination.setAttractions("Observation deck, restaurants, light show");

        assertEquals("DEST003", destination.getDestinationId());
        assertEquals("France", destination.getCountry());
        assertEquals("Eiffel Tower", destination.getName());
        assertEquals("Paris", destination.getCity());
        assertEquals("Iconic iron lattice tower", destination.getDescription());
        assertEquals("Landmark", destination.getCategory());
        assertEquals("Spring", destination.getBestSeasonToVisit());
        assertEquals("Observation deck, restaurants, light show", destination.getAttractions());
    }

    @Test
    @DisplayName("Should return correct toString representation")
    void shouldReturnCorrectToString() {
        TravelDestination destination = TravelDestination.builder()
                .destinationId("DEST004")
                .country("Japan")
                .name("Mount Fuji")
                .city("Fuji")
                .description("Iconic volcano")
                .category("Mountain")
                .bestSeasonToVisit("Autumn")
                .attractions("Climbing, hiking, photography")
                .build();

        String toString = destination.toString();
        
        assertTrue(toString.contains("destinationId='DEST004'"));
        assertTrue(toString.contains("country='Japan'"));
        assertTrue(toString.contains("name='Mount Fuji'"));
        assertTrue(toString.contains("city='Fuji'"));
        assertTrue(toString.contains("description='Iconic volcano'"));
        assertTrue(toString.contains("category='Mountain'"));
        assertTrue(toString.contains("bestSeasonToVisit='Autumn'"));
        assertTrue(toString.contains("attractions='Climbing, hiking, photography'"));
    }

    @Test
    @DisplayName("Should return correct equals and hashCode")
    void shouldReturnCorrectEqualsAndHashCode() {
        TravelDestination destination1 = TravelDestination.builder()
                .destinationId("DEST005")
                .country("Italy")
                .name("Colosseum")
                .build();

        TravelDestination destination2 = TravelDestination.builder()
                .destinationId("DEST005")
                .country("Italy")
                .name("Colosseum")
                .build();

        TravelDestination destination3 = TravelDestination.builder()
                .destinationId("DEST006")
                .country("Italy")
                .name("Colosseum")
                .build();

        // Test equals
        assertEquals(destination1, destination2);
        assertNotEquals(destination1, destination3);
        assertNotEquals(destination1, null);
        assertEquals(destination1, destination1);

        // Test hashCode
        assertEquals(destination1.hashCode(), destination2.hashCode());
        assertNotEquals(destination1.hashCode(), destination3.hashCode());
    }

    @Test
    @DisplayName("Should handle null values in equals")
    void shouldHandleNullValuesInEquals() {
        TravelDestination destination1 = new TravelDestination();
        TravelDestination destination2 = new TravelDestination();
        
        assertEquals(destination1, destination2);
        
        destination1.setDestinationId("DEST007");
        assertNotEquals(destination1, destination2);
    }
}
