################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../src/client/CSocket.cpp \
../src/client/DataReceiver.cpp \
../src/client/Dispatcher.cpp \
../src/client/GaeaClientConfig.cpp \
../src/client/Parameter.cpp \
../src/client/ProxyStandard.cpp \
../src/client/Server.cpp \
../src/client/ServerProfile.cpp \
../src/client/ServiceProxy.cpp \
../src/client/SocketPool.cpp \
../src/client/SocketPoolProfile.cpp \
../src/client/WindowData.cpp 

C_SRCS += \
../src/client/Log.c 

OBJS += \
./src/client/CSocket.o \
./src/client/DataReceiver.o \
./src/client/Dispatcher.o \
./src/client/GaeaClientConfig.o \
./src/client/Log.o \
./src/client/Parameter.o \
./src/client/ProxyStandard.o \
./src/client/Server.o \
./src/client/ServerProfile.o \
./src/client/ServiceProxy.o \
./src/client/SocketPool.o \
./src/client/SocketPoolProfile.o \
./src/client/WindowData.o 

C_DEPS += \
./src/client/Log.d 

CPP_DEPS += \
./src/client/CSocket.d \
./src/client/DataReceiver.d \
./src/client/Dispatcher.d \
./src/client/GaeaClientConfig.d \
./src/client/Parameter.d \
./src/client/ProxyStandard.d \
./src/client/Server.d \
./src/client/ServerProfile.d \
./src/client/ServiceProxy.d \
./src/client/SocketPool.d \
./src/client/SocketPoolProfile.d \
./src/client/WindowData.d 


# Each subdirectory must supply rules for building sources it contributes
src/client/%.o: ../src/client/%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: Cross G++ Compiler'
	g++ -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '

src/client/%.o: ../src/client/%.c
	@echo 'Building file: $<'
	@echo 'Invoking: Cross GCC Compiler'
	gcc -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


