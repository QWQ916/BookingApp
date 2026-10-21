package ru.my.projects.booking.arena;

public record ArenaDTO(
        Long id,
        String name,
        String description,
        String address,
        String cityName
) {
    public static ArenaDTO transfer(Arena arena){
        return new ArenaDTO(arena.getId(), arena.getName(), arena.getDescription(), arena.getAddress(), arena.getCity().getName());
    }
}
