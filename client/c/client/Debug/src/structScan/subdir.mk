################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../src/structScan/FileScan.cpp 

C_SRCS += \
../src/structScan/input.c 

OBJS += \
./src/structScan/FileScan.o \
./src/structScan/input.o 

C_DEPS += \
./src/structScan/input.d 

CPP_DEPS += \
./src/structScan/FileScan.d 


# Each subdirectory must supply rules for building sources it contributes
src/structScan/%.o: ../src/structScan/%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: Cross G++ Compiler'
	g++ -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '

src/structScan/%.o: ../src/structScan/%.c
	@echo 'Building file: $<'
	@echo 'Invoking: Cross GCC Compiler'
	gcc -O0 -g3 -Wall -c -fmessage-length=0 -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


