package net.artren.cobble

import com.tcoded.folialib.FoliaLib
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.concurrent.CompletableFuture

class Cobble : JavaPlugin() {

    var foliaLib: FoliaLib = FoliaLib(this)
    private var aiDisabled = false

    override fun onEnable() {
        foliaLib.scheduler.runTimer({ _ ->
            val playerCount = Bukkit.getOnlinePlayers().size
            if (playerCount > 15 && !aiDisabled) {
                val entitiesToRemove = mutableListOf<LivingEntity>()
                for (entity: Entity in Bukkit.getWorld("world")!!.entities) {
                    if (entity is LivingEntity && entity !is Player) {
                        entitiesToRemove.add(entity)
                    }
                }

                val halfCount = entitiesToRemove.size / 2
                for (i in 0 until halfCount) {
                    val entityToRemove = entitiesToRemove[i]
                    entityToRemove.remove()
                }

                aiDisabled = true
            } else if (playerCount <= 15 && aiDisabled) {
                for (entity: Entity in Bukkit.getWorld("world")!!.entities) {
                    if (entity is LivingEntity && entity !is Player) {
                        entity.setAI(true)
                    }
                }
                aiDisabled = false
            }
        },0L, 100L)
    }

    override fun onDisable() {
        for (entity: Entity in Bukkit.getWorld("world")!!.entities) {
            if (entity is LivingEntity && entity !is Player) {
                entity.setAI(true)
            }
        }
        aiDisabled = false
    }
}